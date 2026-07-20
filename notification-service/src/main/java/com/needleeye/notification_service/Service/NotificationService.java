package com.needleeye.notification_service.Service;

public interface NotificationService {
    void sendRegistrationEmail(String toEmail, String name);
    void sendOtp(String toEmail, String otp);
}
