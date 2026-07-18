package com.needleeye.inventory_service.Service;

public interface KafkaConsumerService {
    void handleProductCreated(String eventData);
    void handleProductDeleted(String eventData);
}
