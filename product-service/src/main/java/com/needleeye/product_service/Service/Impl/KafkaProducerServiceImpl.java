package com.needleeye.product_service.Service.Impl;

import com.needleeye.product_service.Service.KafkaProducerService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerServiceImpl implements KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducerServiceImpl(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void sendProductCreatedEvent(String productId) {
        kafkaTemplate.send("product.created", productId);
        System.out.println("Product create event sent to Kafka: " + productId);
    }

    @Override
    public void sendProductDeleteEvent(String productId) {
        kafkaTemplate.send("product.deleted", productId);
        System.out.println("Product delete event sent to Kafka: " + productId);
    }
}
