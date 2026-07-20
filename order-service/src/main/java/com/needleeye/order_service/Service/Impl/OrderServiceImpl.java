package com.needleeye.order_service.Service.Impl;

import com.needleeye.order_service.Configuration.OpenFeign.UserServiceClient;
import com.needleeye.order_service.Dto.Request.*;
import com.needleeye.order_service.Dto.Response.*;
import com.needleeye.order_service.Entity.Order;
import com.needleeye.order_service.Entity.OrderItem;
import com.needleeye.order_service.Entity.Payment;
import com.needleeye.order_service.Repository.OrderRepo;
import com.needleeye.order_service.Repository.PaymentRepo;
import com.needleeye.order_service.Service.KafkaProducerService;
import com.needleeye.order_service.Service.OrderService;
import com.needleeye.order_service.Utils.Constants.AppConstants;
import com.needleeye.order_service.Utils.Enums.OrderStatus;
import com.needleeye.order_service.Utils.Enums.PaymentStatus;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepo orderRepo;
    private final PaymentRepo paymentRepo;
    private final UserServiceClient userServiceClient;
    private final KafkaProducerService kafkaProducerService;

    // Allowed payment status transitions
    private static final Map<PaymentStatus, EnumSet<PaymentStatus>> ALLOWED_PAYMENT_TRANSITIONS = new EnumMap<>(PaymentStatus.class);

    static {
        ALLOWED_PAYMENT_TRANSITIONS.put(PaymentStatus.PENDING, EnumSet.of(PaymentStatus.PAID, PaymentStatus.FAILED));
        ALLOWED_PAYMENT_TRANSITIONS.put(PaymentStatus.FAILED, EnumSet.of(PaymentStatus.PENDING));
        ALLOWED_PAYMENT_TRANSITIONS.put(PaymentStatus.PAID, EnumSet.of(PaymentStatus.REFUNDED));
        ALLOWED_PAYMENT_TRANSITIONS.put(PaymentStatus.REFUNDED, EnumSet.noneOf(PaymentStatus.class));
    }

    public OrderServiceImpl(OrderRepo orderRepo, PaymentRepo paymentRepo, UserServiceClient userServiceClient, KafkaProducerService kafkaProducerService) {
        this.orderRepo = orderRepo;
        this.paymentRepo = paymentRepo;
        this.userServiceClient = userServiceClient;
        this.kafkaProducerService = kafkaProducerService;
    }

    // Get all orders
    @Override
    public ResponseEntity<ApiResponse<?>> getAllOrders() {
        try {
            List<Order> orders = orderRepo.findAll();
            List<OrderResponseDto> responseList = new ArrayList<>();

            for (Order order : orders) {
                responseList.add(createOrderResponse(order));
            }

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ApiResponse<>(HttpStatus.OK.value(), AppConstants.ORDERS_FETCHED, responseList));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.SERVER_ERROR));
    }

    // Get order by order id
    @Override
    public ResponseEntity<ApiResponse<?>> getOrderById(String orderId) {
        try {
            Optional<Order> optionalOrder = orderRepo.findByOrderId(orderId);

            if (optionalOrder.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), AppConstants.ORDER_NOT_FOUND));
            }

            OrderResponseDto responseDto = createOrderResponse(optionalOrder.get());

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ApiResponse<>(HttpStatus.OK.value(), AppConstants.ORDER_FETCHED, responseDto));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.SERVER_ERROR));
    }

    // Get orders by user id
    @Override
    public ResponseEntity<ApiResponse<?>> getOrdersByUserId(String userId) {
        try {
            userServiceClient.getUserDataById(userId);

            List<Order> orders = orderRepo.findByUserId(userId);
            List<OrderResponseDto> responseList = new ArrayList<>();

            for (Order order : orders) {
                responseList.add(createOrderResponse(order));
            }

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ApiResponse<>(HttpStatus.OK.value(), AppConstants.ORDERS_FETCHED, responseList));

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

    // Get order payment details
    @Override
    public ResponseEntity<ApiResponse<?>> getPaymentDetailsByOrderId(String orderId) {
        try {
            Optional<Order> optionalOrder = orderRepo.findByOrderId(orderId);

            if (optionalOrder.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), AppConstants.ORDER_NOT_FOUND));
            }

            Optional<Payment> payment = paymentRepo.findByOrder_OrderId(orderId);

            if (payment.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), AppConstants.PAYMENT_NOT_FOUND));
            }

            PaymentResponseDto responseDto = createPaymentResponse(payment.get());

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ApiResponse<>(HttpStatus.OK.value(), AppConstants.PAYMENT_FETCHED, responseDto));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.SERVER_ERROR));
    }

    // Update order status
    @Override
    public ResponseEntity<ApiResponse<?>> updateOrderStatus(String orderId, OrderStatusUpdateDto statusData) {
        try {
            Optional<Order> optionalOrder = orderRepo.findByOrderId(orderId);

            if (optionalOrder.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), AppConstants.ORDER_NOT_FOUND));
            }

            Order order = optionalOrder.get();
            OrderStatus currentStatus = order.getStatus();
            OrderStatus newStatus = statusData.getStatus();

            if (currentStatus == newStatus) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), AppConstants.INVALID_ORDER_STATUS_TRANSITION));
            }

            order.setStatus(newStatus);
            order.setUpdatedAt(LocalDateTime.now());
            orderRepo.save(order);

            OrderResponseDto responseDto = createOrderResponse(order);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ApiResponse<>(HttpStatus.OK.value(), AppConstants.ORDER_STATUS_UPDATED, responseDto));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.SERVER_ERROR));
    }

    @Override
    public ResponseEntity<ApiResponse<?>> updateOrderPaymentStatus(String orderId, PaymentStatusUpdateDto paymentStatusData) {
        try {
            Optional<Order> optionalOrder = orderRepo.findByOrderId(orderId);

            if (optionalOrder.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), AppConstants.ORDER_NOT_FOUND));
            }

            Optional<Payment> payment = paymentRepo.findByOrder_OrderId(orderId);

            if (payment.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), AppConstants.PAYMENT_NOT_FOUND));
            }

            PaymentStatus currentStatus = payment.get().getPaymentStatus();
            PaymentStatus newStatus = paymentStatusData.getPaymentStatus();

            if (currentStatus != newStatus && !ALLOWED_PAYMENT_TRANSITIONS.get(currentStatus).contains(newStatus)) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), AppConstants.INVALID_PAYMENT_STATUS_TRANSITION));
            }

            payment.get().setPaymentStatus(newStatus);

            if (paymentStatusData.getTransactionId() != null) {
                payment.get().setTransactionId(paymentStatusData.getTransactionId());
            }

            if (newStatus == PaymentStatus.PAID) {
                payment.get().setPaidDateAndTime(LocalDateTime.now());
            }

            payment.get().setUpdatedAt(LocalDateTime.now());

            paymentRepo.save(payment.get());

            PaymentResponseDto responseDto = createPaymentResponse(payment.get());

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ApiResponse<>(HttpStatus.OK.value(), AppConstants.PAYMENT_STATUS_UPDATED, responseDto));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.SERVER_ERROR));
    }

    // Create order
    @Override
    public ResponseEntity<ApiResponse<?>> createOrder(String userId, CreateOrderDto orderData) {
        try {
            ResponseEntity<ApiResponse<UserResponseDataDto>> userResponse = userServiceClient.getUserDataById(userId);
            UserResponseDataDto userResponseData = userResponse.getBody().getData();

            if (userResponseData == null) {
                return ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(new ApiResponse<>(HttpStatus.CREATED.value(), AppConstants.USER_NOT_FOUND));
            }

            // Get email and name
            String email = userResponseData.getEmail();
            String name = userResponseData.getFirstName();

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

            // Send mail to user when order placed
            sendOrderReplacedEvent(order, email, name);

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

    // Delete order
    @Override
    public ResponseEntity<ApiResponse<?>> deleteOrder(String orderId) {
        try {
            Optional<Order> optionalOrder = orderRepo.findByOrderId(orderId);

            if (optionalOrder.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), AppConstants.ORDER_NOT_FOUND));
            }

            orderRepo.delete(optionalOrder.get());

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ApiResponse<>(HttpStatus.OK.value(), AppConstants.ORDER_DELETED));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.SERVER_ERROR));
    }

    // Send order replaced event
    private void sendOrderReplacedEvent(Order order, String email, String name) {
        List<OrderItemEventDto> orderItemEventDtoList = new ArrayList<>();
        OrderPlacedEventDto orderPlacedEventDto = new OrderPlacedEventDto();

        for (OrderItem item : order.getItems()) {
            OrderItemEventDto eventDto = new OrderItemEventDto();

            eventDto.setProductName(item.getProductName());
            eventDto.setQuantity(item.getQuantity());
            eventDto.setPrice(item.getPrice());
            eventDto.setSubtotal(item.getSubtotal());

            orderItemEventDtoList.add(eventDto);
        }

        orderPlacedEventDto.setOrderId(order.getOrderId());
        orderPlacedEventDto.setUserId(order.getUserId());
        orderPlacedEventDto.setEmail(email);
        orderPlacedEventDto.setCustomerName(name);
        orderPlacedEventDto.setShippingAddress(order.getShippingAddress());
        orderPlacedEventDto.setPaymentMethod(order.getPayment().getPaymentMethod().toString());
        orderPlacedEventDto.setTotalAmount(order.getTotalAmount());
        orderPlacedEventDto.setItems(orderItemEventDtoList);

        kafkaProducerService.sendOrderPlacedEvent(orderPlacedEventDto);
    }

    // Create order response
    private OrderResponseDto createOrderResponse(Order order) {
        OrderResponseDto orderData = new OrderResponseDto();
        List<OrderItemResponseDto> itemData = new ArrayList<>();
        PaymentResponseDto paymentData = new PaymentResponseDto();

        // Set oder item data as list
        for (OrderItem item : order.getItems()) {
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

    // Create payment details response
    private PaymentResponseDto createPaymentResponse(Payment payment) {
        PaymentResponseDto paymentData = new PaymentResponseDto();

        // Set payment details
        paymentData.setPaymentId(payment.getPaymentId());
        paymentData.setPaymentMethod(payment.getPaymentMethod());
        paymentData.setPaymentStatus(payment.getPaymentStatus());
        paymentData.setAmount(payment.getAmount());
        paymentData.setTransactionId(payment.getTransactionId());
        paymentData.setPaidDateAndTime(payment.getPaidDateAndTime());
        paymentData.setCreatedAt(payment.getCreatedAt());
        paymentData.setUpdatedAt(payment.getUpdatedAt());

        return paymentData;
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
