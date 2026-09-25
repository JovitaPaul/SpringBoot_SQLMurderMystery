package com.sqlmurdermystery.queryexec.service;

import com.sqlmurdermystery.queryexec.config.QueryExecutionProperties;
import com.sqlmurdermystery.queryexec.dto.QueryRequest;
import com.sqlmurdermystery.queryexec.dto.QueryResultDto;
import com.sqlmurdermystery.queryexec.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class QueryExecutionService {

    private final QueryExecutionProperties properties;
    private final SqlSafetyValidator safetyValidator;

    public QueryExecutionService(QueryExecutionProperties properties, SqlSafetyValidator safetyValidator) {
        this.properties = properties;
        this.safetyValidator = safetyValidator;
    }

    public QueryResultDto execute(QueryRequest request) throws SQLException {
        safetyValidator.validateSchemaName(request.getTargetSchema());
        String cleanSql = safetyValidator.validateAndClean(request.getSql());

        String jdbcUrl = "jdbc:mysql://" + properties.getDbHost() + ":" + properties.getDbPort()
                + "/" + request.getTargetSchema()
                + "?useSSL=false&allowMultiQueries=false&connectTimeout=3000&socketTimeout="
                + (properties.getTimeoutSeconds() * 1000 + 2000);

        long start = System.currentTimeMillis();

        // A fresh connection per request, opened with the SELECT-only 'smm_readonly'
        // account — not the application's normal DB user — so a validator bug can't
        // turn into a write. See SqlSafetyValidator's class-level note.
        try (Connection connection = DriverManager.getConnection(
                jdbcUrl, properties.getReadonlyUsername(), properties.getReadonlyPassword())) {

            connection.setReadOnly(true);

            try (Statement statement = connection.createStatement()) {
                statement.setQueryTimeout(properties.getTimeoutSeconds());
                // Fetch one extra row so we can tell the learner their result was truncated.
                statement.setMaxRows(properties.getMaxRows() + 1);

                try (ResultSet resultSet = statement.executeQuery(cleanSql)) {
                    return toResultDto(resultSet, start);
                }
            }
        } catch (SQLTimeoutException e) {
            throw new ApiException(HttpStatus.REQUEST_TIMEOUT,
                    "Query took longer than " + properties.getTimeoutSeconds() + "s and was cancelled. "
                            + "Try narrowing it down (add a WHERE clause, fewer JOINs, etc.).");
        } catch (SQLNonTransientConnectionException | SQLTransientConnectionException e) {
            throw new ApiException(HttpStatus.SERVICE_UNAVAILABLE,
                    "Could not reach the case database right now — please try again.");
        }
    }

    private QueryResultDto toResultDto(ResultSet resultSet, long startMs) throws SQLException {
        ResultSetMetaData meta = resultSet.getMetaData();
        int columnCount = meta.getColumnCount();

        List<String> columns = new ArrayList<>(columnCount);
        for (int i = 1; i <= columnCount; i++) {
            columns.add(meta.getColumnLabel(i));
        }

        List<List<Object>> rows = new ArrayList<>();
        boolean truncated = false;
        int maxRows = properties.getMaxRows();

        while (resultSet.next()) {
            if (rows.size() >= maxRows) {
                truncated = true;
                break;
            }
            List<Object> row = new ArrayList<>(columnCount);
            for (int i = 1; i <= columnCount; i++) {
                row.add(resultSet.getObject(i));
            }
            rows.add(row);
        }

        long elapsed = System.currentTimeMillis() - startMs;
        return new QueryResultDto(columns, rows, rows.size(), elapsed, truncated);
    }
}
