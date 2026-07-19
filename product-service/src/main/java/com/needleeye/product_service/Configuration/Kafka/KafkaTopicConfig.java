package com.needleeye.product_service.Configuration.Kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {
    @Bean
    public NewTopic createProductCreateTopic() {
        return new NewTopic("product.created", 3, (short) 1);
    }

    @Bean
    public NewTopic createProductDeletedTopic() {
        return new NewTopic("product.deleted", 3, (short) 1);
    }
}
