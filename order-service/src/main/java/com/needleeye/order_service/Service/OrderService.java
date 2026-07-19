package com.needleeye.order_service.Service;

import com.needleeye.order_service.Dto.Request.CreateOrderDto;
import com.needleeye.order_service.Dto.Request.OrderStatusUpdateDto;
import com.needleeye.order_service.Dto.Request.PaymentStatusUpdateDto;
import com.needleeye.order_service.Dto.Response.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface OrderService {
    ResponseEntity<ApiResponse<?>> getAllOrders();
    ResponseEntity<ApiResponse<?>> getOrderById(String orderId);
    ResponseEntity<ApiResponse<?>> getOrdersByUserId(String userId);
    ResponseEntity<ApiResponse<?>> getPaymentDetailsByOrderId(String orderId);
    ResponseEntity<ApiResponse<?>> createOrder(String userId, CreateOrderDto orderData);
    ResponseEntity<ApiResponse<?>> updateOrderStatus(String orderId, OrderStatusUpdateDto statusData);
    ResponseEntity<ApiResponse<?>> updateOrderPaymentStatus(String orderId, PaymentStatusUpdateDto paymentStatusData);
    ResponseEntity<ApiResponse<?>> deleteOrder(String orderId);

}
