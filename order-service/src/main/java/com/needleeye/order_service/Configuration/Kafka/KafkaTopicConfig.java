package com.needleeye.order_service.Configuration.Kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {
    @Bean
    public NewTopic createOrderPlacedTopic() {
        return new NewTopic("order.placed", 3, (short) 1);
    }

    @Bean
    public NewTopic createOrderStatusUpdatedTopic() {
        return new NewTopic("order.status-updated", 3, (short) 1);
    }
}
