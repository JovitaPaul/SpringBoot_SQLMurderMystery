package com.sqlmurdermystery.notification.service;

import com.sqlmurdermystery.notification.dto.EmailEventRequest;
import com.sqlmurdermystery.notification.model.EmailEventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;
    private final String from;

    public EmailService(JavaMailSender mailSender,
                        @Value("${notification.email.from:}") String from) {
        this.mailSender = mailSender;
        this.from = from;
    }

    public void send(EmailEventRequest event) {
        String name = event.getName() == null || event.getName().isBlank() ? "there" : event.getName();
        String subject;
        String body;

        switch (event.getEventType()) {
            case WELCOME_EMAIL -> {
                subject = "Welcome to SQL Murder Mystery";
                body = "Hi " + name + ",\n\nWelcome to SQL Murder Mystery! Your account is ready.\n\nHappy investigating!";
            }
            case INACTIVITY -> {
                subject = "We miss you at SQL Murder Mystery";
                body = "Hi " + name + ",\n\nIt has been a while since your last activity. Come back and continue solving SQL mysteries when you are ready!";
            }
            case PASSWORD_CHANGED -> {
                subject = "Your password was changed";
                body = "Hi " + name + ",\n\nYour SQL Murder Mystery password was successfully changed. If you did not make this change, please contact support immediately.";
            }
            default -> throw new IllegalArgumentException("Unsupported email event: " + event.getEventType());
        }

        SimpleMailMessage message = new SimpleMailMessage();
        if (from != null && !from.isBlank()) {
            message.setFrom(from);
        }
        message.setTo(event.getEmail());
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
        log.info("Sent {} email to {}", event.getEventType(), event.getEmail());
    }
}
