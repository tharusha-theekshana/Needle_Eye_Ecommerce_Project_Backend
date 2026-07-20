package com.needleeye.notification_service.Service.Impl;

import com.needleeye.notification_service.Dto.OrderPlacedEventDto;
import com.needleeye.notification_service.Dto.OrderStatusUpdateEventDto;
import com.needleeye.notification_service.Dto.OtpEventDto;
import com.needleeye.notification_service.Dto.UserRegisterEventDto;
import com.needleeye.notification_service.Service.KafkaConsumerService;
import com.needleeye.notification_service.Service.NotificationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerServiceImpl implements KafkaConsumerService {

    private NotificationService notificationService;

    public KafkaConsumerServiceImpl(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    @KafkaListener(topics = "user.register", groupId = "notification-service")
    public void handleUserRegistered(UserRegisterEventDto event) {
        notificationService.sendRegistrationEmail(event.getEmail(),event.getName());
    }

    @Override
    @KafkaListener(topics = "user.otp", groupId = "notification-service")
    public void handleUserOtpEvent(OtpEventDto event) {
        notificationService.sendOtp(event.getEmail(),event.getOtp());
    }

    @Override
    @KafkaListener(topics = "order.placed", groupId = "notification-service")
    public void handleOrderPlaced(OrderPlacedEventDto event) {
        notificationService.sendOrderPlacedEmail(event);
    }

    @Override
    @KafkaListener(topics = "user.otp", groupId = "notification-service")
    public void handleOrderStatusUpdated(OrderStatusUpdateEventDto event) {
        notificationService.sendOrderStatusUpdateEmail(event);
    }
}
