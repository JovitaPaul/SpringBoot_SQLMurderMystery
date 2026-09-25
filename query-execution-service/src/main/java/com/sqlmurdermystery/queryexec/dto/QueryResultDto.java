package com.sqlmurdermystery.queryexec.dto;

import java.util.List;

public class QueryResultDto {
    private List<String> columns;
    private List<List<Object>> rows;
    private int rowCount;
    private long executionTimeMs;
    private boolean truncated;   // true if more rows existed than max-rows allows

    public QueryResultDto() {}

    public QueryResultDto(List<String> columns, List<List<Object>> rows, int rowCount,
                           long executionTimeMs, boolean truncated) {
        this.columns = columns;
        this.rows = rows;
        this.rowCount = rowCount;
        this.executionTimeMs = executionTimeMs;
        this.truncated = truncated;
    }

    public List<String> getColumns() { return columns; }
    public void setColumns(List<String> columns) { this.columns = columns; }

    public List<List<Object>> getRows() { return rows; }
    public void setRows(List<List<Object>> rows) { this.rows = rows; }

    public int getRowCount() { return rowCount; }
    public void setRowCount(int rowCount) { this.rowCount = rowCount; }

    public long getExecutionTimeMs() { return executionTimeMs; }
    public void setExecutionTimeMs(long executionTimeMs) { this.executionTimeMs = executionTimeMs; }

    public boolean isTruncated() { return truncated; }
    public void setTruncated(boolean truncated) { this.truncated = truncated; }
}
