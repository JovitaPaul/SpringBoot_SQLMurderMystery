package com.sqlmurdermystery.progress.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "services")
public class ServiceUrlsProperties {

    private String leaderboardUrl;
    private String notificationUrl;

    public String getLeaderboardUrl() { return leaderboardUrl; }
    public void setLeaderboardUrl(String leaderboardUrl) { this.leaderboardUrl = leaderboardUrl; }

    public String getNotificationUrl() { return notificationUrl; }
    public void setNotificationUrl(String notificationUrl) { this.notificationUrl = notificationUrl; }
}