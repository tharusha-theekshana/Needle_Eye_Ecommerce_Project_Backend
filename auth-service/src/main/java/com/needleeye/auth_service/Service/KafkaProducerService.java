package com.needleeye.auth_service.Service;

import com.needleeye.auth_service.Dto.Request.OtpEventDto;
import com.needleeye.auth_service.Dto.Request.UserRegisterEventDto;


public interface KafkaProducerService {
    void sendUserRegisteredEvent(UserRegisterEventDto userRegisterEvent);
    void sendUserOtpEvent(OtpEventDto otpEventDto);
}
