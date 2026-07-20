package com.needleeye.notification_service.Service;

import com.needleeye.notification_service.Dto.OrderPlacedEventDto;
import com.needleeye.notification_service.Dto.OrderStatusUpdateEventDto;
import com.needleeye.notification_service.Dto.OtpEventDto;
import com.needleeye.notification_service.Dto.UserRegisterEventDto;

public interface KafkaConsumerService {
    void handleUserRegistered(UserRegisterEventDto event);
    void handleUserOtpEvent(OtpEventDto event);
    void handleOrderPlaced(OrderPlacedEventDto event);
    void handleOrderStatusUpdated(OrderStatusUpdateEventDto event);
}
