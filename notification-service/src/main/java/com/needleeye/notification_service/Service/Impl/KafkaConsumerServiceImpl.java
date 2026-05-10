package com.needleeye.notification_service.Service.Impl;

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
}
