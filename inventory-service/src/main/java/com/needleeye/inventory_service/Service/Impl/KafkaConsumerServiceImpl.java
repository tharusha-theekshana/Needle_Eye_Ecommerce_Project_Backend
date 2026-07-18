package com.needleeye.inventory_service.Service.Impl;

import com.needleeye.inventory_service.Service.InventoryService;
import com.needleeye.inventory_service.Service.KafkaConsumerService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerServiceImpl implements KafkaConsumerService {
    private InventoryService inventoryService;

    public KafkaConsumerServiceImpl(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @Override
    @KafkaListener(topics = "product.created", groupId = "inventory-service")
    public void handleProductCreated(String eventData) {
        String productId = eventData;
        inventoryService.createInventoryForProduct(productId);
    }

    @Override
    @KafkaListener(topics = "product.deleted", groupId = "inventory-service")
    public void handleProductDeleted(String eventData) {
        String productId = eventData;
        inventoryService.deleteInventoryByProductId(productId);
    }
}
