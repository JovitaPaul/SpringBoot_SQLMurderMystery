package com.sqlmurdermystery.notification.dto;

import jakarta.validation.constraints.NotBlank;

public class NotificationEventRequest {

    @NotBlank(message = "title is required")
    private String title;

    @NotBlank(message = "message is required")
    private String message;

    public NotificationEventRequest() {}

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
