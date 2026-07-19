package com.needleeye.order_service.Service;

import com.needleeye.order_service.Dto.Request.CreateOrderDto;
import com.needleeye.order_service.Dto.Response.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface OrderService {
    ResponseEntity<ApiResponse<?>> createOrder(String userId, CreateOrderDto orderData);
}
