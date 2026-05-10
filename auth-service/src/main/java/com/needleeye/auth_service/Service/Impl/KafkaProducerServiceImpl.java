package com.needleeye.auth_service.Service.Impl;

import com.needleeye.auth_service.Dto.Request.UserRegisterEventDto;
import com.needleeye.auth_service.Service.KafkaProducerService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerServiceImpl implements KafkaProducerService {

    private final KafkaTemplate<String, UserRegisterEventDto> kafkaTemplate;

    public KafkaProducerServiceImpl(KafkaTemplate<String, UserRegisterEventDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void sendUserRegisteredEvent(UserRegisterEventDto userRegisterEvent) {
        kafkaTemplate.send("user.register", userRegisterEvent);
        System.out.println("Event sent to Kafka: " + userRegisterEvent.getUserId());
    }
}
