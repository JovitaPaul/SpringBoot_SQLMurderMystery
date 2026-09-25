package com.sqlmurdermystery.notification.model;

import java.time.Instant;

/**
 * Held in memory only (see NotificationStore) — docker-compose gives this service no
 * database, matching the proposal's "sends... alerts" scope rather than a persisted
 * inbox. Restarting the service clears notifications; that's an accepted trade-off
 * for a course project, not an oversight.
 */
public class Notification {

    private final long id;
    private final String username;
    private final String title;
    private final String message;
    private final Instant createdAt;
    private volatile boolean read;

    public Notification(long id, String username, String title, String message) {
        this.id = id;
        this.username = username;
        this.title = title;
        this.message = message;
        this.createdAt = Instant.now();
        this.read = false;
    }

    public long getId() { return id; }
    public String getUsername() { return username; }
    public String getTitle() { return title; }
    public String getMessage() { return message; }
    public Instant getCreatedAt() { return createdAt; }
    public boolean isRead() { return read; }
    public void setRead(boolean read) { this.read = read; }
}
