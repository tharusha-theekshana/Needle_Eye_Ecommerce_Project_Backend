package com.needleeye.order_service.Dto.Request;

public class OrderStatusUpdateEventDto {
    private String orderId;
    private String userId;
    private String email;
    private String customerName;
    private String previousStatus;
    private String newStatus;
    private Double totalAmount;

    public OrderStatusUpdateEventDto() {
    }

    public OrderStatusUpdateEventDto(String orderId, String userId, String email, String customerName, String previousStatus, String newStatus, Double totalAmount) {
        this.orderId = orderId;
        this.userId = userId;
        this.email = email;
        this.customerName = customerName;
        this.previousStatus = previousStatus;
        this.newStatus = newStatus;
        this.totalAmount = totalAmount;
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

    public String getPreviousStatus() {
        return previousStatus;
    }

    public void setPreviousStatus(String previousStatus) {
        this.previousStatus = previousStatus;
    }

    public String getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(String newStatus) {
        this.newStatus = newStatus;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
