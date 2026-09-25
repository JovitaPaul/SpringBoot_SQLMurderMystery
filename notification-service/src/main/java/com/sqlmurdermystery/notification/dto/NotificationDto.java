package com.sqlmurdermystery.notification.dto;

import java.time.Instant;

public class NotificationDto {
    private long id;
    private String title;
    private String message;
    private boolean read;
    private Instant createdAt;

    public NotificationDto() {}

    public NotificationDto(long id, String title, String message, boolean read, Instant createdAt) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.read = read;
        this.createdAt = createdAt;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public boolean isRead() { return read; }
    public void setRead(boolean read) { this.read = read; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
