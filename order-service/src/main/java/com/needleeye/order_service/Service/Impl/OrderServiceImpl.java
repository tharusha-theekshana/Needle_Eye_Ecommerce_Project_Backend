package com.needleeye.order_service.Service.Impl;

import com.needleeye.order_service.Configuration.OpenFeign.UserServiceClient;
import com.needleeye.order_service.Dto.Request.CreateOrderDto;
import com.needleeye.order_service.Dto.Request.OrderItemDto;
import com.needleeye.order_service.Dto.Response.ApiResponse;
import com.needleeye.order_service.Dto.Response.OrderItemResponseDto;
import com.needleeye.order_service.Dto.Response.OrderResponseDto;
import com.needleeye.order_service.Dto.Response.PaymentResponseDto;
import com.needleeye.order_service.Entity.Order;
import com.needleeye.order_service.Entity.OrderItem;
import com.needleeye.order_service.Entity.Payment;
import com.needleeye.order_service.Repository.OrderRepo;
import com.needleeye.order_service.Repository.PaymentRepo;
import com.needleeye.order_service.Service.OrderService;
import com.needleeye.order_service.Utils.Constants.AppConstants;
import com.needleeye.order_service.Utils.Enums.OrderStatus;
import com.needleeye.order_service.Utils.Enums.PaymentStatus;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepo orderRepo;
    private final PaymentRepo paymentRepo;
    private final UserServiceClient userServiceClient;

    public OrderServiceImpl(OrderRepo orderRepo, PaymentRepo paymentRepo, UserServiceClient userServiceClient) {
        this.orderRepo = orderRepo;
        this.paymentRepo = paymentRepo;
        this.userServiceClient = userServiceClient;
    }

    // Create order
    @Override
    public ResponseEntity<ApiResponse<?>> createOrder(String userId, CreateOrderDto orderData) {
        try {
            userServiceClient.getUserDataById(userId);

            Order order = new Order();
            order.setOrderId(generateUniqueOrderId());
            order.setUserId(userId);
            order.setShippingAddress(orderData.getShippingAddress());
            order.setStatus(OrderStatus.PENDING);
            order.setCreatedAt(LocalDateTime.now());
            order.setUpdatedAt(LocalDateTime.now());

            List<OrderItem> orderItems = orderData.getItems()
                    .stream()
                    .map(itemDto -> mapDtoToOrderItemEntity(itemDto, order))
                    .collect(Collectors.toList());

            double totalAmount = orderItems.stream().mapToDouble(OrderItem::getSubtotal).sum();

            order.setItems(orderItems);
            order.setTotalAmount(totalAmount);

            Payment payment = new Payment();
            payment.setPaymentId(generateUniquePaymentId());
            payment.setOrder(order);
            payment.setPaymentMethod(orderData.getPaymentMethod());
            payment.setPaymentStatus(PaymentStatus.PENDING);
            payment.setAmount(totalAmount);
            payment.setCreatedAt(LocalDateTime.now());
            payment.setUpdatedAt(LocalDateTime.now());

            order.setPayment(payment);

            orderRepo.save(order);
            OrderResponseDto responseDto = createOrderResponse(order);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(HttpStatus.CREATED.value(), AppConstants.ORDER_ADDED, responseDto));

        } catch (FeignException.NotFound e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), AppConstants.USER_NOT_FOUND));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.SERVER_ERROR));
    }

    private OrderResponseDto createOrderResponse(Order order){
        OrderResponseDto orderData = new OrderResponseDto();
        List<OrderItemResponseDto> itemData = new ArrayList<>();
        PaymentResponseDto paymentData = new PaymentResponseDto();

        // Set oder item data as list
        for(OrderItem item : order.getItems()){
            OrderItemResponseDto orderItemResponseDto = new OrderItemResponseDto();

            orderItemResponseDto.setItemId(item.getId());
            orderItemResponseDto.setProductId(item.getProductId());
            orderItemResponseDto.setProductName(item.getProductName());
            orderItemResponseDto.setImageUrl(item.getImageUrl());
            orderItemResponseDto.setPrice(item.getPrice());
            orderItemResponseDto.setSize(item.getSize());
            orderItemResponseDto.setColor(item.getColor());
            orderItemResponseDto.setQuantity(item.getQuantity());
            orderItemResponseDto.setSubtotal(item.getSubtotal());

            itemData.add(orderItemResponseDto);
        }

        // Set payment details
        paymentData.setPaymentId(order.getPayment().getPaymentId());
        paymentData.setPaymentMethod(order.getPayment().getPaymentMethod());
        paymentData.setPaymentStatus(order.getPayment().getPaymentStatus());
        paymentData.setAmount(order.getPayment().getAmount());
        paymentData.setTransactionId(order.getPayment().getTransactionId());
        paymentData.setPaidDateAndTime(order.getPayment().getPaidDateAndTime());
        paymentData.setCreatedAt(order.getPayment().getCreatedAt());
        paymentData.setUpdatedAt(order.getPayment().getUpdatedAt());

        orderData.setId(order.getId());
        orderData.setOrderId(order.getOrderId());
        orderData.setUserId(order.getUserId());
        orderData.setItems(itemData);
        orderData.setTotalAmount(order.getTotalAmount());
        orderData.setStatus(order.getStatus());
        orderData.setShippingAddress(order.getShippingAddress());
        orderData.setPayment(paymentData);
        orderData.setCreatedAt(order.getCreatedAt());
        orderData.setUpdatedAt(order.getUpdatedAt());

        return orderData;
    }

    // Map order item dto to entity
    private OrderItem mapDtoToOrderItemEntity(OrderItemDto itemDto, Order order) {
        OrderItem item = new OrderItem();

        item.setOrder(order);
        item.setProductId(itemDto.getProductId());
        item.setProductName(itemDto.getProductName());
        item.setImageUrl(itemDto.getImageUrl());
        item.setPrice(itemDto.getPrice());
        item.setSize(itemDto.getSize());
        item.setColor(itemDto.getColor());
        item.setQuantity(itemDto.getQuantity());
        item.setSubtotal(itemDto.getPrice() * itemDto.getQuantity());
        return item;
    }


    // Generate order id
    private String generateUniqueOrderId() {
        String orderId;
        do {
            int number = new Random().nextInt(900000) + 100000;
            orderId = "ORD" + number;
        } while (orderRepo.existsByOrderId(orderId));
        return orderId;
    }

    // Generate payment id
    private String generateUniquePaymentId() {
        String paymentId;
        do {
            int number = new Random().nextInt(900000) + 100000;
            paymentId = "PAY" + number;
        } while (paymentRepo.existsByPaymentId(paymentId));
        return paymentId;
    }
}
