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

/** See LeaderboardClient's class-level note — same forwarded-bearer-token pattern. */
@Component
public class NotificationClient {

    private static final Logger log = LoggerFactory.getLogger(NotificationClient.class);

    private final RestTemplate restTemplate;
    private final ServiceUrlsProperties serviceUrls;

    public NotificationClient(RestTemplate restTemplate, ServiceUrlsProperties serviceUrls) {
        this.restTemplate = restTemplate;
        this.serviceUrls = serviceUrls;
    }

    public void sendEvent(String bearerToken, String title, String message) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", bearerToken);
            restTemplate.postForEntity(
                    serviceUrls.getNotificationUrl() + "/api/notifications/events",
                    new HttpEntity<>(Map.of("title", title, "message", message), headers),
                    Void.class
            );
        } catch (RestClientException e) {
            log.warn("Could not send notification: {}", e.getMessage());
        }
    }
}
