package com.needleeye.auth_service.Configuration.Kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {
    @Bean
    public NewTopic createNewTopic() {
        return new NewTopic("user.register", 3, (short) 1);
    }

    @Bean
    public NewTopic createOtpTopic() {
        return new NewTopic("user.otp", 3, (short) 1);
    }
}
