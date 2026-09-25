package com.sqlmurdermystery.notification.controller;

import com.sqlmurdermystery.notification.dto.NotificationDto;
import com.sqlmurdermystery.notification.dto.NotificationEventRequest;
import com.sqlmurdermystery.notification.model.Notification;
import com.sqlmurdermystery.notification.service.NotificationStore;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationStore notificationStore;

    public NotificationController(NotificationStore notificationStore) {
        this.notificationStore = notificationStore;
    }

    /** Called by progress-tracking-service (with the learner's own forwarded bearer
     *  token) after a quiz/case completion, to raise an achievement/streak alert. */
    @PostMapping("/events")
    public ResponseEntity<NotificationDto> createEvent(@Valid @RequestBody NotificationEventRequest request,
                                                         Authentication authentication) {
        Notification notification = notificationStore.add(authentication.getName(), request.getTitle(), request.getMessage());
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(notification));
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
