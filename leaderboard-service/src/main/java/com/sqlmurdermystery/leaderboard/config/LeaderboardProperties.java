package com.sqlmurdermystery.leaderboard.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "leaderboard")
public class LeaderboardProperties {

    private String redisKey;

    public String getRedisKey() { return redisKey; }
    public void setRedisKey(String redisKey) { this.redisKey = redisKey; }
}
