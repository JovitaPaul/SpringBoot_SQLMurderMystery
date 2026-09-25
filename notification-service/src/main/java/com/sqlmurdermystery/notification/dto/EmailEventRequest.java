package com.sqlmurdermystery.notification.dto;

import com.sqlmurdermystery.notification.model.EmailEventType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class EmailEventRequest {
    @NotNull
    private EmailEventType eventType;

    @NotBlank
    @Email
    private String email;

    private String name;

    public EmailEventRequest() {}

    public EmailEventType getEventType() { return eventType; }
    public void setEventType(EmailEventType eventType) { this.eventType = eventType; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
