package com.sqlmurdermystery.auth.service;

import com.sqlmurdermystery.auth.model.User;
import com.sqlmurdermystery.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
public class InactivityEmailScheduler {
    private final UserRepository userRepository;
    private final NotificationEmailClient notificationEmailClient;
    private final long inactivityDays;

    public InactivityEmailScheduler(UserRepository userRepository,
                                    NotificationEmailClient notificationEmailClient,
                                    @Value("${notification.inactivity-days:7}") long inactivityDays) {
        this.userRepository = userRepository;
        this.notificationEmailClient = notificationEmailClient;
        this.inactivityDays = inactivityDays;
    }

    @Scheduled(cron = "${notification.inactivity-cron:0 0 9 * * *}")
    @Transactional
    public void sendInactivityEmails() {
        Instant cutoff = Instant.now().minus(inactivityDays, ChronoUnit.DAYS);
        for (User user : userRepository.findByLastLoginAtBeforeAndLastInactivityEmailAtIsNull(cutoff)) {
            send(user);
        }
        for (User user : userRepository.findByLastLoginAtBeforeAndLastInactivityEmailAtBefore(cutoff, cutoff)) {
            send(user);
        }
    }

    private void send(User user) {
        notificationEmailClient.send("INACTIVITY", user);
        user.setLastInactivityEmailAt(Instant.now());
        userRepository.save(user);
    }
}
