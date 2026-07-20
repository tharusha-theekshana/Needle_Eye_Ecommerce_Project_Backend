package com.needleeye.order_service.Service;

import com.needleeye.order_service.Dto.Request.OrderPlacedEventDto;
import com.needleeye.order_service.Dto.Request.OrderStatusUpdateEventDto;

public interface KafkaProducerService {
    void sendOrderPlacedEvent(OrderPlacedEventDto orderPlacedEvent);
    void sendOrderStatusUpdatedEvent(OrderStatusUpdateEventDto orderStatusUpdateEvent);
}
