package com.sqlmurdermystery.queryexec.dto;

import jakarta.validation.constraints.NotBlank;

public class QueryRequest {

    @NotBlank(message = "targetSchema is required")
    private String targetSchema;

    @NotBlank(message = "sql is required")
    private String sql;

    public QueryRequest() {}

    public String getTargetSchema() { return targetSchema; }
    public void setTargetSchema(String targetSchema) { this.targetSchema = targetSchema; }

    public String getSql() { return sql; }
    public void setSql(String sql) { this.sql = sql; }
}
