package com.sqlmurdermystery.progress.service;

import com.sqlmurdermystery.progress.config.ServiceUrlsProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * Thin REST client to leaderboard-service. There is no message broker in this stack
 * (docker-compose only provisions MySQL + Redis), so cross-service "events" here are
 * plain synchronous REST calls, made best-effort so a leaderboard hiccup never blocks
 * or loses a learner's own progress record.
 *
 * The learner's own bearer token is forwarded rather than inventing a separate
 * unauthenticated "internal" endpoint — leaderboard-service applies its normal JWT
 * auth and reads the username from the token, exactly like every other request.
 */
@Component
public class LeaderboardClient {

    private static final Logger log = LoggerFactory.getLogger(LeaderboardClient.class);

    private final RestTemplate restTemplate;
    private final ServiceUrlsProperties serviceUrls;

    public LeaderboardClient(RestTemplate restTemplate, ServiceUrlsProperties serviceUrls) {
        this.restTemplate = restTemplate;
        this.serviceUrls = serviceUrls;
    }

    public void addScore(String bearerToken, int points) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", bearerToken);
            restTemplate.postForEntity(
                    serviceUrls.getLeaderboardUrl() + "/api/leaderboard/score",
                    new HttpEntity<>(Map.of("points", points), headers),
                    Void.class
            );
        } catch (RestClientException e) {
            log.warn("Could not update leaderboard: {}", e.getMessage());
        }
    }
}
