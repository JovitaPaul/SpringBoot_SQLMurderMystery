package com.sqlmurdermystery.notification.service;

import com.sqlmurdermystery.notification.model.Notification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class NotificationStore {

    private final Map<String, List<Notification>> byUsername = new ConcurrentHashMap<>();
    private final AtomicLong idSequence = new AtomicLong(1);

    public Notification add(String username, String title, String message) {
        Notification notification = new Notification(idSequence.getAndIncrement(), username, title, message);
        byUsername.computeIfAbsent(username, u -> new CopyOnWriteArrayList<>()).add(0, notification);
        return notification;
    }

    public List<Notification> listFor(String username) {
        return byUsername.getOrDefault(username, List.of());
    }

    /** @return true if a notification with this id existed and belonged to the user. */
    public boolean markRead(String username, long notificationId) {
        for (Notification n : listFor(username)) {
            if (n.getId() == notificationId) {
                n.setRead(true);
                return true;
            }
        }
        return false;
    }
}
