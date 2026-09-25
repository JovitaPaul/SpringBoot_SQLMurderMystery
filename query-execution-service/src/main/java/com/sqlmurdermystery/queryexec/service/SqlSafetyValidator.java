package com.sqlmurdermystery.queryexec.service;

import com.sqlmurdermystery.queryexec.config.QueryExecutionProperties;
import com.sqlmurdermystery.queryexec.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Application-level defense: rejects anything that isn't a single, plain SELECT
 * (or WITH ... SELECT) statement before it ever reaches the database.
 *
 * This is intentionally the FIRST of two independent layers — the second is that
 * QueryExecutionService only ever connects with the 'smm_readonly' MySQL account
 * (SELECT-only grants) and opens a session-level read-only transaction. Either layer
 * alone would stop a destructive query; together they mean a bug in this regex-based
 * validator can't turn into data loss.
 */
@Component
public class SqlSafetyValidator {

    private static final Pattern LINE_COMMENT = Pattern.compile("--.*?(\\r?\\n|$)");
    private static final Pattern BLOCK_COMMENT = Pattern.compile("/\\*.*?\\*/", Pattern.DOTALL);
    private static final Pattern STARTS_WITH_SELECT_OR_WITH =
            Pattern.compile("^\\(*\\s*(SELECT|WITH)\\b", Pattern.CASE_INSENSITIVE);

    /** Statement types and admin/file operations that must never reach the DB, even
     *  though the DB grants would also block most of these. */
    private static final List<String> FORBIDDEN_KEYWORDS = List.of(
            "INSERT", "UPDATE", "DELETE", "DROP", "ALTER", "CREATE", "TRUNCATE", "REPLACE",
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

        String noComments = BLOCK_COMMENT.matcher(rawSql).replaceAll(" ");
        noComments = LINE_COMMENT.matcher(noComments).replaceAll(" ");

        List<String> statements = List.of(noComments.split(";")).stream()
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();

        if (statements.isEmpty()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "sql must not be blank");
        }
        if (statements.size() > 1) {
            throw new ApiException(HttpStatus.BAD_REQUEST,
                    "Only a single statement is allowed — remove the extra statement(s) after the semicolon.");
        }

        String statement = statements.get(0);

        if (!STARTS_WITH_SELECT_OR_WITH.matcher(statement).find()) {
            throw new ApiException(HttpStatus.BAD_REQUEST,
                    "Only read-only SELECT queries are allowed here.");
        }

        String upper = statement.toUpperCase();
        for (String keyword : FORBIDDEN_KEYWORDS) {
            if (containsWord(upper, keyword)) {
                throw new ApiException(HttpStatus.BAD_REQUEST,
                        "\"" + keyword + "\" is not allowed in a read-only query.");
            }
        }
        for (String phrase : FORBIDDEN_PHRASES) {
            if (upper.contains(phrase)) {
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
