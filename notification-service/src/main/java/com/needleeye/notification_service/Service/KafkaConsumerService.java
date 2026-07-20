package com.needleeye.notification_service.Service;

import com.needleeye.notification_service.Dto.OtpEventDto;
import com.needleeye.notification_service.Dto.UserRegisterEventDto;

public interface KafkaConsumerService {
    void handleUserRegistered(UserRegisterEventDto event);
    void handleUserOtpEvent(OtpEventDto event);
}
