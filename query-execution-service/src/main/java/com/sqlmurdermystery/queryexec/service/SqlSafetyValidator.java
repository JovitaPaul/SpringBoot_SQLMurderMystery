package com.sqlmurdermystery.queryexec.service;

import com.sqlmurdermystery.queryexec.config.QueryExecutionProperties;
import com.sqlmurdermystery.queryexec.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Application-level defense. Only single, read-only statements are allowed:
 *   SELECT / WITH ... SELECT, SHOW (TABLES, COLUMNS, CREATE TABLE, INDEX, ...),
 *   DESCRIBE / DESC / EXPLAIN.
 * Any reference to a protected table (e.g. "solution") is rejected.
 *
 * This is the FIRST of two layers; the second is the 'smm_readonly' MySQL account,
 * which has SELECT only on the learner-visible tables (see
 * mysql-init/05-readonly-grants-defaultdb.sql) and no privileges on "solution".
 */
@Component
public class SqlSafetyValidator {

    private static final Pattern LINE_COMMENT = Pattern.compile("(--[ \\t].*?|#.*?)(\\r?\\n|$)");
    private static final Pattern BLOCK_COMMENT = Pattern.compile("/\\*.*?\\*/", Pattern.DOTALL);

    private static final Pattern ALLOWED_START = Pattern.compile(
            "^\\(*\\s*(SELECT|WITH|SHOW|DESCRIBE|DESC|EXPLAIN)\\b", Pattern.CASE_INSENSITIVE);

    /** Forbidden anywhere in the statement. CREATE is checked separately so that
     *  "SHOW CREATE TABLE x" is still possible. */
    private static final List<String> FORBIDDEN_KEYWORDS = List.of(
            "INSERT", "UPDATE", "DELETE", "DROP", "ALTER", "TRUNCATE", "REPLACE",
            "GRANT", "REVOKE", "EXEC", "EXECUTE", "CALL", "MERGE", "SET", "LOCK", "UNLOCK",
            "LOAD_FILE", "SLEEP", "BENCHMARK", "SHUTDOWN", "HANDLER", "PREPARE", "DEALLOCATE",
            "RENAME", "OUTFILE", "DUMPFILE", "ANALYZE", "OPTIMIZE", "REPAIR", "KILL", "USE"
    );

    private static final List<String> FORBIDDEN_PHRASES = List.of(
            "INTO OUTFILE", "INTO DUMPFILE", "FOR UPDATE", "FOR SHARE", "INFORMATION_SCHEMA",
            "MYSQL.", "PERFORMANCE_SCHEMA", "SYS."
    );

    /** SHOW variants that leak server/account details or are otherwise not useful. */
    private static final Pattern FORBIDDEN_SHOW = Pattern.compile(
            "^\\s*SHOW\\s+(GRANTS|PROCESSLIST|FULL\\s+PROCESSLIST|VARIABLES|STATUS|GLOBAL|SESSION|"
                    + "BINARY|MASTER|SLAVE|REPLICA|PLUGINS|PRIVILEGES|ENGINE|ENGINES|OPEN|PROFILE|"
                    + "EVENTS|TRIGGERS|PROCEDURE|FUNCTION|CREATE\\s+(USER|DATABASE|SCHEMA|PROCEDURE|FUNCTION|EVENT|TRIGGER))\\b",
            Pattern.CASE_INSENSITIVE);

    /** Tables learners must never touch (they hold the answers). */
    private static final List<String> PROTECTED_TABLES = List.of("solution", "solutions");

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

        if (!ALLOWED_START.matcher(statement).find()) {
            throw new ApiException(HttpStatus.BAD_REQUEST,
                    "Only read-only queries are allowed: SELECT, SHOW, DESCRIBE and EXPLAIN.");
        }

        String upper = statement.toUpperCase();

        if (FORBIDDEN_SHOW.matcher(statement).find()) {
            throw new ApiException(HttpStatus.BAD_REQUEST,
                    "That SHOW command is not available. Try SHOW TABLES, SHOW COLUMNS FROM <table>, "
                            + "SHOW CREATE TABLE <table> or SHOW INDEX FROM <table>.");
        }

        // CREATE is only legal as part of SHOW CREATE TABLE
        String withoutShowCreate = upper.replaceAll("^\\s*SHOW\\s+CREATE\\s+TABLE\\b", " ");
        if (containsWord(withoutShowCreate, "CREATE")) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "\"CREATE\" is not allowed in a read-only query.");
        }

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

        for (String table : PROTECTED_TABLES) {
            // matches solution, `solution`, "solution", db.solution, "solution_x" is NOT matched (word boundary)
            if (Pattern.compile("(?<![A-Za-z0-9_$])[`\"']?" + Pattern.quote(table.toUpperCase()) + "[`\"']?(?![A-Za-z0-9_$])")
                    .matcher(upper).find()) {
                throw new ApiException(HttpStatus.FORBIDDEN,
                        "Access to the \"" + table + "\" table is restricted — solve the case yourself!");
            }
        }

        return statement;
    }

    /** Only the shared case database or schemas with the case prefix may be queried. */
    public void validateSchemaName(String schemaName) {
        if (schemaName == null || !schemaName.matches("^[a-zA-Z0-9_]{1,64}$")) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid targetSchema.");
        }

        if (!schemaName.startsWith(properties.getAllowedSchemaPrefix())
                && !schemaName.equals("defaultdb")) {
            throw new ApiException(HttpStatus.FORBIDDEN,
                    "targetSchema must be a case schema or defaultdb.");
        }
    }

    private boolean containsWord(String haystackUpper, String wordUpper) {
        return Pattern.compile("\\b" + Pattern.quote(wordUpper) + "\\b").matcher(haystackUpper).find();
    }
}
