package com.needleeye.order_service.Service.Impl;

import com.needleeye.order_service.Dto.Request.OrderPlacedEventDto;
import com.needleeye.order_service.Dto.Request.OrderStatusUpdateEventDto;
import com.needleeye.order_service.Service.KafkaProducerService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerServiceImpl implements KafkaProducerService {
    private final KafkaTemplate<String, OrderPlacedEventDto> kafkaOrderPlacedTemplate;
    private final KafkaTemplate<String, OrderStatusUpdateEventDto> kafkaOrderStatusTemplate;

    public KafkaProducerServiceImpl(KafkaTemplate<String, OrderPlacedEventDto> kafkaOrderPlacedTemplate, KafkaTemplate<String, OrderStatusUpdateEventDto> kafkaOrderStatusTemplate) {
        this.kafkaOrderPlacedTemplate = kafkaOrderPlacedTemplate;
        this.kafkaOrderStatusTemplate = kafkaOrderStatusTemplate;
    }

    @Override
    public void sendOrderPlacedEvent(OrderPlacedEventDto orderPlacedEvent) {
        kafkaOrderPlacedTemplate.send("order.placed", orderPlacedEvent);
        System.out.println("Order placed event sent to Kafka: " + orderPlacedEvent.getOrderId());
    }

    @Override
    public void sendOrderStatusUpdatedEvent(OrderStatusUpdateEventDto orderStatusUpdateEvent) {
        kafkaOrderStatusTemplate.send("order.status-updated", orderStatusUpdateEvent);
        System.out.println("Order status update event sent to Kafka: " + orderStatusUpdateEvent.getOrderId());
    }
}
