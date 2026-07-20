package com.needleeye.order_service.Dto.Request;

import java.util.List;

public class OrderPlacedEventDto {
    private String orderId;
    private String userId;
    private String email;
    private String customerName;
    private String shippingAddress;
    private String paymentMethod;
    private Double totalAmount;
    private List<OrderItemEventDto> items;

    public OrderPlacedEventDto() {
    }

    public OrderPlacedEventDto(String orderId, String userId, String email, String customerName, String shippingAddress, String paymentMethod, Double totalAmount, List<OrderItemEventDto> items) {
        this.orderId = orderId;
        this.userId = userId;
        this.email = email;
        this.customerName = customerName;
        this.shippingAddress = shippingAddress;
        this.paymentMethod = paymentMethod;
        this.totalAmount = totalAmount;
        this.items = items;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<OrderItemEventDto> getItems() {
        return items;
    }

    public void setItems(List<OrderItemEventDto> items) {
        this.items = items;
    }
}
