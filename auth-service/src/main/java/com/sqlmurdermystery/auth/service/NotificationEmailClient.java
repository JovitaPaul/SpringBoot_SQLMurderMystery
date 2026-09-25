package com.sqlmurdermystery.auth.service;

import com.sqlmurdermystery.auth.config.NotificationProperties;
import com.sqlmurdermystery.auth.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class NotificationEmailClient {
    private static final Logger log = LoggerFactory.getLogger(NotificationEmailClient.class);

    private final RestTemplate restTemplate;
    private final NotificationProperties properties;

    public NotificationEmailClient(RestTemplate restTemplate, NotificationProperties properties) {
        this.restTemplate = restTemplate;
        this.properties = properties;
    }

    public void send(String eventType, User user) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-Internal-Token", properties.getInternalToken());
            restTemplate.postForEntity(
                    properties.getUrl() + "/api/notifications/email-events",
                    new HttpEntity<>(Map.of(
                            "eventType", eventType,
                            "email", user.getEmail(),
                            "name", user.getName()), headers),
                    Void.class);
        } catch (RestClientException e) {
            log.warn("Could not send {} email for {}: {}", eventType, user.getUsername(), e.getMessage());
        }
    }
}
