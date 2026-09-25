package com.sqlmurdermystery.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "notification")
public class NotificationProperties {
    private String url;
    private String internalToken;

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public String getInternalToken() { return internalToken; }
    public void setInternalToken(String internalToken) { this.internalToken = internalToken; }
}
