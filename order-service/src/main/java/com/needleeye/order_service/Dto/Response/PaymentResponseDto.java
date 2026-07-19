package com.needleeye.order_service.Dto.Response;

import com.needleeye.order_service.Utils.Enums.PaymentMethod;
import com.needleeye.order_service.Utils.Enums.PaymentStatus;

import java.time.LocalDateTime;

public class PaymentResponseDto {
    private String paymentId;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private Double amount;
    private String transactionId;
    private LocalDateTime paidDateAndTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public PaymentResponseDto() {
    }

    public PaymentResponseDto(String paymentId, PaymentMethod paymentMethod, PaymentStatus paymentStatus, Double amount, String transactionId, LocalDateTime paidDateAndTime, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.paymentId = paymentId;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.amount = amount;
        this.transactionId = transactionId;
        this.paidDateAndTime = paidDateAndTime;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public LocalDateTime getPaidDateAndTime() {
        return paidDateAndTime;
    }

    public void setPaidDateAndTime(LocalDateTime paidDateAndTime) {
        this.paidDateAndTime = paidDateAndTime;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
