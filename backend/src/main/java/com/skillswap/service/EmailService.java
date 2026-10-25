package com.skillswap.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * Sends transactional emails. Wrapped in try/catch so that a missing/misconfigured
 * SMTP setup (e.g. running locally without mail credentials) never breaks the main flow -
 * it just logs instead of throwing.
 */
@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);
    private final Optional<JavaMailSender> mailSender;

    public EmailService(Optional<JavaMailSender> mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendWelcomeEmail(String to, String name) {
        send(to, "Welcome to Skill Swap!",
                "Hi " + name + ",\n\nWelcome to Skill Swap Marketplace! You've received free starter credits. " +
                        "Start by listing skills you can teach and skills you want to learn.\n\nHappy swapping!");
    }

    @Async
    public void sendBookingRequestEmail(String to, String teacherName, String learnerName, String skillName) {
        send(to, "New session request on Skill Swap",
                "Hi " + teacherName + ",\n\n" + learnerName + " has requested a session with you to learn "
                        + skillName + ". Log in to accept or decline.");
    }

    @Async
    public void sendBookingStatusEmail(String to, String recipientName, String skillName, String status) {
        send(to, "Your session request was " + status.toLowerCase(),
                "Hi " + recipientName + ",\n\nYour session for " + skillName + " has been " + status.toLowerCase() + ".");
    }

    @Async
    public void sendSessionReminderEmail(String to, String recipientName, String skillName, LocalDateTime scheduledAt) {
        String time = scheduledAt.format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a"));
        send(to, "Reminder: your session starts soon",
                "Hi " + recipientName + ",\n\nJust a reminder that your session for " + skillName +
                        " is scheduled at " + time + ".");
    }

    @Async
    public void sendReviewPromptEmail(String to, String recipientName, String skillName) {
        send(to, "How was your session?",
                "Hi " + recipientName + ",\n\nYour session for " + skillName +
                        " is complete. Please leave a review for your session partner!");
    }

    private void send(String to, String subject, String text) {
        try {
            if (mailSender.isEmpty()) {
                log.info("Email configuration not available; skipping email to {}", to);
                return;
            }
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            mailSender.get().send(message);
        } catch (Exception e) {
            // In local/dev environments without SMTP configured, don't crash the request flow.
            log.warn("Could not send email to {}: {}", to, e.getMessage());
        }
    }
}
