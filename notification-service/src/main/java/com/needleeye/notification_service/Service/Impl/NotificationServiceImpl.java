package com.needleeye.notification_service.Service.Impl;

import com.needleeye.notification_service.Dto.OrderPlacedEventDto;
import com.needleeye.notification_service.Dto.OrderStatusUpdateEventDto;
import com.needleeye.notification_service.Service.NotificationService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
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

    @Override
    public void sendOrderPlacedEmail(OrderPlacedEventDto event) {
        if (event.getEmail() == null || event.getEmail().isBlank()) {
            System.out.println("Skipping order placed email - no email on file for order: " + event.getOrderId());
            return;
        }

        Context context = new Context();
        context.setVariable("customerName", event.getCustomerName());
        context.setVariable("orderId", event.getOrderId());
        context.setVariable("items", event.getItems());
        context.setVariable("totalAmount", event.getTotalAmount());
        context.setVariable("shippingAddress", event.getShippingAddress());
        context.setVariable("paymentMethod", event.getPaymentMethod());

        String htmlBody = templateEngine.process("order-placed", context);
        sendMail(event.getEmail(), "Your Order " + event.getOrderId() + " Has Been Placed 🛍️", htmlBody);
    }

    @Override
    public void sendOrderStatusUpdateEmail(OrderStatusUpdateEventDto event) {
        if (event.getEmail() == null || event.getEmail().isBlank()) {
            System.out.println("Skipping order status update email - no email on file for order: " + event.getOrderId());
            return;
        }

        Context context = new Context();
        context.setVariable("customerName", event.getCustomerName());
        context.setVariable("orderId", event.getOrderId());
        context.setVariable("previousStatus", event.getPreviousStatus());
        context.setVariable("newStatus", event.getNewStatus());
        context.setVariable("totalAmount", event.getTotalAmount());

        String htmlBody = templateEngine.process("order-status-updated", context);
        sendMail(event.getEmail(), "Order " + event.getOrderId() + " Status Updated", htmlBody);
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
