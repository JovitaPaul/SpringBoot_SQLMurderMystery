package com.sqlmurdermystery.notification.controller;

import com.sqlmurdermystery.notification.dto.EmailEventRequest;
import com.sqlmurdermystery.notification.dto.NotificationDto;
import com.sqlmurdermystery.notification.dto.NotificationEventRequest;
import com.sqlmurdermystery.notification.model.Notification;
import com.sqlmurdermystery.notification.service.EmailService;
import com.sqlmurdermystery.notification.service.NotificationStore;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationStore notificationStore;
    private final EmailService emailService;
    private final String internalToken;

    public NotificationController(NotificationStore notificationStore,
                                  EmailService emailService,
                                  @Value("${notification.internal-token}") String internalToken) {
        this.notificationStore = notificationStore;
        this.emailService = emailService;
        this.internalToken = internalToken;
    }

    @PostMapping("/events")
    public ResponseEntity<NotificationDto> createEvent(@Valid @RequestBody NotificationEventRequest request,
                                                         Authentication authentication) {
        Notification notification = notificationStore.add(authentication.getName(), request.getTitle(), request.getMessage());
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(notification));
    }

    /** Internal service-to-service endpoint for transactional emails. */
    @PostMapping("/email-events")
    public ResponseEntity<Void> createEmailEvent(@RequestHeader("X-Internal-Token") String token,
                                                  @Valid @RequestBody EmailEventRequest request) {
        if (!internalToken.equals(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        emailService.send(request);
        return ResponseEntity.accepted().build();
    }

    @GetMapping
    public ResponseEntity<List<NotificationDto>> listMyNotifications(Authentication authentication) {
        List<NotificationDto> notifications = notificationStore.listFor(authentication.getName()).stream()
                .map(this::toDto)
                .toList();
        return ResponseEntity.ok(notifications);
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<Void> markRead(@PathVariable long id, Authentication authentication) {
        boolean found = notificationStore.markRead(authentication.getName(), id);
        return found ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    private NotificationDto toDto(Notification n) {
        return new NotificationDto(n.getId(), n.getTitle(), n.getMessage(), n.isRead(), n.getCreatedAt());
    }
}
