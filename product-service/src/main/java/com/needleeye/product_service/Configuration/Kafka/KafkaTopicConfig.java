package com.needleeye.product_service.Configuration.Kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {
    @Bean
    public NewTopic createNewTopic() {
        return new NewTopic("product.created", 3, (short) 1);
    }
}
