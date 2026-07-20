package com.needleeye.notification_service.Service.Impl;

import com.needleeye.notification_service.Service.NotificationService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public NotificationServiceImpl(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void sendRegistrationEmail(String toEmail, String name) {
        Context context = new Context();
        context.setVariable("name", name);

        String htmlBody = templateEngine.process("registration-success", context);
        sendMail(toEmail, "Welcome! Registration Successful 🎉", htmlBody);
    }

    @Override
    public void sendOtp(String toEmail, String otp) {
        Context context = new Context();
        context.setVariable("otp", otp);
        context.setVariable("expiryMinutes", 5);

        String htmlBody = templateEngine.process("forgot-password", context);
        sendMail(toEmail, "Your Password Reset OTP", htmlBody);
    }

    // Send mail with html template
    private void sendMail(String toEmail, String subject, String htmlBody) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);

            mailSender.send(message);
            System.out.println("Email sent to: " + toEmail);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
