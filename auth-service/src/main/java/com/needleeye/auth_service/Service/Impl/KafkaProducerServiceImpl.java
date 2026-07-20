package com.needleeye.auth_service.Service.Impl;

import com.needleeye.auth_service.Dto.Request.OtpEventDto;
import com.needleeye.auth_service.Dto.Request.UserRegisterEventDto;
import com.needleeye.auth_service.Service.KafkaProducerService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerServiceImpl implements KafkaProducerService {

    private final KafkaTemplate<String, UserRegisterEventDto> kafkaUserRegisterTemplate;
    private final KafkaTemplate<String, OtpEventDto> kafkaOtpTemplate;

    public KafkaProducerServiceImpl(KafkaTemplate<String, UserRegisterEventDto> kafkaUserRegisterTemplate, KafkaTemplate<String, OtpEventDto> kafkaOtpTemplate) {
        this.kafkaUserRegisterTemplate = kafkaUserRegisterTemplate;
        this.kafkaOtpTemplate = kafkaOtpTemplate;
    }

    @Override
    public void sendUserRegisteredEvent(UserRegisterEventDto userRegisterEvent) {
        kafkaUserRegisterTemplate.send("user.register", userRegisterEvent);
        System.out.println("Event sent to Kafka: " + userRegisterEvent.getUserId());
    }

    @Override
    public void sendUserOtpEvent(OtpEventDto otpEventDto) {
        kafkaOtpTemplate.send("user.otp", otpEventDto);
        System.out.println("Event sent to Kafka: " + otpEventDto.getUserId());
    }
}
