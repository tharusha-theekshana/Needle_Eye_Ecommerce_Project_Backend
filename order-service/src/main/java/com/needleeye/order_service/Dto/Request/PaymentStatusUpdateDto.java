package com.needleeye.order_service.Dto.Request;

import com.needleeye.order_service.Utils.Enums.PaymentStatus;
import jakarta.validation.constraints.NotNull;

public class PaymentStatusUpdateDto {
    @NotNull(message = "Payment status is required")
    private PaymentStatus paymentStatus;

    private String transactionId;

    public PaymentStatusUpdateDto() {
    }

    public PaymentStatusUpdateDto(@NotNull(message = "Payment status is required") PaymentStatus paymentStatus, String transactionId) {
        this.paymentStatus = paymentStatus;
        this.transactionId = transactionId;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}
