package com.needleeye.order_service.Repository;

import com.needleeye.order_service.Entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepo extends JpaRepository<Payment,Long> {
    boolean existsByPaymentId(String paymentId);
    Optional<Payment> findByOrder_OrderId(String orderId);
}
