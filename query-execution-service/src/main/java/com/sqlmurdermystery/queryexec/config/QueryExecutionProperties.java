package com.sqlmurdermystery.queryexec.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "query.execution")
public class QueryExecutionProperties {

    private String dbHost;
    private int dbPort;
    private String readonlyUsername;
    private String readonlyPassword;
    private String allowedSchemaPrefix;
    private int timeoutSeconds;
    private int maxRows;

    public String getDbHost() { return dbHost; }
    public void setDbHost(String dbHost) { this.dbHost = dbHost; }

    public int getDbPort() { return dbPort; }
    public void setDbPort(int dbPort) { this.dbPort = dbPort; }

    public String getReadonlyUsername() { return readonlyUsername; }
    public void setReadonlyUsername(String readonlyUsername) { this.readonlyUsername = readonlyUsername; }

    public String getReadonlyPassword() { return readonlyPassword; }
    public void setReadonlyPassword(String readonlyPassword) { this.readonlyPassword = readonlyPassword; }

    public String getAllowedSchemaPrefix() { return allowedSchemaPrefix; }
    public void setAllowedSchemaPrefix(String allowedSchemaPrefix) { this.allowedSchemaPrefix = allowedSchemaPrefix; }

    public int getTimeoutSeconds() { return timeoutSeconds; }
    public void setTimeoutSeconds(int timeoutSeconds) { this.timeoutSeconds = timeoutSeconds; }

    public int getMaxRows() { return maxRows; }
    public void setMaxRows(int maxRows) { this.maxRows = maxRows; }
}
