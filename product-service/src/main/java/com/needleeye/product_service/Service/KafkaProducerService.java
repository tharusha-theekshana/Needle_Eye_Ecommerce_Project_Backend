package com.needleeye.product_service.Service;

public interface KafkaProducerService {
    void sendProductCreatedEvent(String productId);
    void sendProductDeleteEvent(String productId);
}