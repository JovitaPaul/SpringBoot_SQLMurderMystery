package com.sqlmurdermystery.queryexec.service;

import com.sqlmurdermystery.queryexec.config.QueryExecutionProperties;
import com.sqlmurdermystery.queryexec.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Application-level defense: rejects anything that isn't a single, plain SELECT
 * (or WITH ... SELECT) statement before it ever reaches the database.
 *
 * This is intentionally the FIRST of two independent layers — the second is that
 * QueryExecutionService only ever connects with the 'smm_readonly' MySQL account
 * (SELECT-only grants) and opens a session-level read-only transaction. Either layer
 * alone would stop a destructive query; together they mean a bug in this validator
 * can't turn into data loss.
 *
 * CHANGED: the old version used regexes over the raw text, so a perfectly valid query
 * such as  SELECT * FROM witness_statement WHERE statement LIKE '%call%'  was rejected
 * (the word CALL inside a string literal), and a ';' or '--' inside a string literal
 * was treated as a statement separator / comment. This version scans the text
 * character by character, understands quotes, and only applies the keyword checks to
 * the SQL *outside* string literals.
 */
@Component
public class SqlSafetyValidator {

    private static final Pattern STARTS_WITH_SELECT_OR_WITH =
            Pattern.compile("^\\(*\\s*(SELECT|WITH)\\b", Pattern.CASE_INSENSITIVE);

    /** Statement types and admin/file operations that must never reach the DB, even
     *  though the DB grants would also block most of these.
     *  (REPLACE was removed: it is also a harmless string function, REPLACE(col,'a','b'),
     *  and a REPLACE *statement* can't pass the must-start-with-SELECT/WITH check.) */
    private static final List<String> FORBIDDEN_KEYWORDS = List.of(
            "INSERT", "UPDATE", "DELETE", "DROP", "ALTER", "CREATE", "TRUNCATE",
            "GRANT", "REVOKE", "EXEC", "EXECUTE", "CALL", "MERGE", "SET", "LOCK", "UNLOCK",
            "LOAD_FILE", "SLEEP", "BENCHMARK", "SHUTDOWN"
    );

    private static final List<String> FORBIDDEN_PHRASES = List.of(
            "INTO OUTFILE", "INTO DUMPFILE", "FOR UPDATE", "FOR SHARE"
    );

    private final QueryExecutionProperties properties;

    public SqlSafetyValidator(QueryExecutionProperties properties) {
        this.properties = properties;
    }

    /** @return the single, comment-free statement that is safe to execute as-is. */
    public String validateAndClean(String rawSql) {
        if (rawSql == null || rawSql.isBlank()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "sql must not be blank");
        }

        // Two parallel strings: `real` is what will be executed (comments removed),
        // `masked` is the same text with the CONTENT of string literals / quoted
        // identifiers blanked out, used only for the keyword checks.
        List<String> realStatements = new ArrayList<>();
        List<String> maskedStatements = new ArrayList<>();
        StringBuilder real = new StringBuilder();
        StringBuilder masked = new StringBuilder();

        int n = rawSql.length();
        int i = 0;
        while (i < n) {
            char c = rawSql.charAt(i);
            char next = i + 1 < n ? rawSql.charAt(i + 1) : '\0';

            // /* block comment */
            if (c == '/' && next == '*') {
                int end = rawSql.indexOf("*/", i + 2);
                i = (end < 0) ? n : end + 2;
                real.append(' ');
                masked.append(' ');
                continue;
            }
            // "-- comment" (MySQL requires whitespace/end after the two dashes) or "# comment"
            boolean dashComment = c == '-' && next == '-'
                    && (i + 2 >= n || Character.isWhitespace(rawSql.charAt(i + 2)));
            if (dashComment || c == '#') {
                while (i < n && rawSql.charAt(i) != '\n') i++;
                real.append(' ');
                masked.append(' ');
                continue;
            }
            // quoted string / identifier: copy through verbatim, but mask for checks
            if (c == '\'' || c == '"' || c == '`') {
                char quote = c;
                real.append(c);
                masked.append(c);
                i++;
                boolean closed = false;
                while (i < n) {
                    char q = rawSql.charAt(i);
                    if (q == '\\' && quote != '`' && i + 1 < n) {   // backslash escape
                        real.append(q).append(rawSql.charAt(i + 1));
                        masked.append("  ");
                        i += 2;
                        continue;
                    }
                    if (q == quote) {
                        if (i + 1 < n && rawSql.charAt(i + 1) == quote) {   // doubled quote = escaped quote
                            real.append(q).append(q);
                            masked.append("  ");
                            i += 2;
                            continue;
                        }
                        real.append(q);
                        masked.append(q);
                        i++;
                        closed = true;
                        break;
                    }
                    real.append(q);
                    masked.append(' ');
                    i++;
                }
                if (!closed) {
                    throw new ApiException(HttpStatus.BAD_REQUEST,
                            "Unclosed quote in your query — check your ' or \" characters.");
                }
                continue;
            }
            // statement separator (only counts when it is outside quotes)
            if (c == ';') {
                realStatements.add(real.toString().trim());
                maskedStatements.add(masked.toString().trim());
                real.setLength(0);
                masked.setLength(0);
                i++;
                continue;
            }
            real.append(c);
            masked.append(c);
            i++;
        }
        realStatements.add(real.toString().trim());
        maskedStatements.add(masked.toString().trim());

        List<String> statements = new ArrayList<>();
        List<String> maskedList = new ArrayList<>();
        for (int k = 0; k < realStatements.size(); k++) {
            if (!realStatements.get(k).isEmpty()) {
                statements.add(realStatements.get(k));
                maskedList.add(maskedStatements.get(k));
            }
        }

        if (statements.isEmpty()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "sql must not be blank");
        }
        if (statements.size() > 1) {
            throw new ApiException(HttpStatus.BAD_REQUEST,
                    "Only a single statement is allowed — remove the extra statement(s) after the semicolon.");
        }

        String statement = statements.get(0);
        String upper = maskedList.get(0).toUpperCase();

        if (!STARTS_WITH_SELECT_OR_WITH.matcher(statement).find()) {
            throw new ApiException(HttpStatus.BAD_REQUEST,
                    "Only read-only SELECT queries are allowed here.");
        }

        for (String keyword : FORBIDDEN_KEYWORDS) {
            if (containsWord(upper, keyword)) {
                throw new ApiException(HttpStatus.BAD_REQUEST,
                        "\"" + keyword + "\" is not allowed in a read-only query.");
            }
        }
        String squashed = upper.replaceAll("\\s+", " ");
        for (String phrase : FORBIDDEN_PHRASES) {
            if (squashed.contains(phrase)) {
                throw new ApiException(HttpStatus.BAD_REQUEST,
                        "\"" + phrase + "\" is not allowed in a read-only query.");
            }
        }

        return statement;
    }

    /** Only schemas seeded for case-solving (e.g. "case_gallery_theft") may be queried. */
    public void validateSchemaName(String schemaName) {
        if (schemaName == null || !schemaName.matches("^[a-zA-Z0-9_]{1,64}$")) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid targetSchema.");
        }
        if (!schemaName.startsWith(properties.getAllowedSchemaPrefix())) {
            throw new ApiException(HttpStatus.FORBIDDEN,
                    "targetSchema must be a case schema (prefix '" + properties.getAllowedSchemaPrefix() + "').");
        }
    }

    private boolean containsWord(String haystackUpper, String wordUpper) {
        return Pattern.compile("\\b" + Pattern.quote(wordUpper) + "\\b").matcher(haystackUpper).find();
    }
}
