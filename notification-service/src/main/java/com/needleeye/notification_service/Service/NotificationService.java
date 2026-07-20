package com.needleeye.notification_service.Service;

import com.needleeye.notification_service.Dto.OrderPlacedEventDto;
import com.needleeye.notification_service.Dto.OrderStatusUpdateEventDto;

public interface NotificationService {
    void sendRegistrationEmail(String toEmail, String name);
    void sendOtp(String toEmail, String otp);
    void sendOrderPlacedEmail(OrderPlacedEventDto event);
    void sendOrderStatusUpdateEmail(OrderStatusUpdateEventDto event);
}
