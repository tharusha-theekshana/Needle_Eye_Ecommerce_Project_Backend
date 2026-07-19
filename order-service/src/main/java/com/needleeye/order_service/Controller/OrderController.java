package com.needleeye.order_service.Controller;

import com.needleeye.order_service.Dto.Request.CreateOrderDto;
import com.needleeye.order_service.Dto.Request.OrderStatusUpdateDto;
import com.needleeye.order_service.Dto.Request.PaymentStatusUpdateDto;
import com.needleeye.order_service.Dto.Response.ApiResponse;
import com.needleeye.order_service.Service.OrderService;
import com.needleeye.order_service.Utils.Constants.AppConstants;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping()
    public ResponseEntity<ApiResponse<?>> getAllOrders() {
        try {
            return orderService.getAllOrders();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.SERVER_ERROR));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<?>> getOrderById(@PathVariable String orderId) {
        try {
            return orderService.getOrderById(orderId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.SERVER_ERROR));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<?>> getOrdersByUserId(@PathVariable String userId) {
        try {
            return orderService.getOrdersByUserId(userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.SERVER_ERROR));
    }

    @GetMapping("/{orderId}/payment-details")
    public ResponseEntity<ApiResponse<?>> getPaymentDetailsByOrderId(@PathVariable String orderId) {
        try {
            return orderService.getPaymentDetailsByOrderId(orderId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.SERVER_ERROR));
    }

    @PostMapping("/{userId}")
    public ResponseEntity<ApiResponse<?>> createOrder(@PathVariable String userId, @Valid @RequestBody CreateOrderDto orderData) {
        try {
            return orderService.createOrder(userId, orderData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.SERVER_ERROR));
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<ApiResponse<?>> updateOrderStatus(@PathVariable String orderId, @Valid @RequestBody OrderStatusUpdateDto statusData) {
        try {
            return orderService.updateOrderStatus(orderId, statusData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.SERVER_ERROR));
    }

    @PatchMapping("/{orderId}/payment")
    public ResponseEntity<ApiResponse<?>> updatePaymentStatus(@PathVariable String orderId, @Valid @RequestBody PaymentStatusUpdateDto paymentStatusData) {
        try {
            return orderService.updateOrderPaymentStatus(orderId, paymentStatusData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.SERVER_ERROR));
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<ApiResponse<?>> deleteOrder(@PathVariable String orderId) {
        try {
            return orderService.deleteOrder(orderId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.SERVER_ERROR));
    }



}
