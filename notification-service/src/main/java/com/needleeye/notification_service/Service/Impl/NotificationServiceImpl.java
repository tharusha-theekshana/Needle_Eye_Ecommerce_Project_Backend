package com.needleeye.notification_service.Service.Impl;

import com.needleeye.notification_service.Service.NotificationService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final JavaMailSender mailSender;

    public NotificationServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendRegistrationEmail(String toEmail, String name) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Welcome! Registration Successful 🎉");
        message.setText("Hi " + name + ",\n\nYour registration was successful. Welcome aboard!\n\nThanks,\nTeam");

        mailSender.send(message);
        System.out.println("Email sent to: " + toEmail);
    }
}
