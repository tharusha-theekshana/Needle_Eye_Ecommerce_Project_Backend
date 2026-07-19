package com.needleeye.order_service.Controller;

import com.needleeye.order_service.Dto.Request.CreateOrderDto;
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
}
