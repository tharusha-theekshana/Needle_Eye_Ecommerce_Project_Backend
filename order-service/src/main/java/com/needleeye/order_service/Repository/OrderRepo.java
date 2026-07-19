package com.needleeye.order_service.Repository;

import com.needleeye.order_service.Entity.Order;
import com.needleeye.order_service.Entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepo extends JpaRepository<Order,Long> {
    boolean existsByOrderId(String orderId);
    Optional<Order> findByOrderId(String orderId);
    List<Order> findByUserId(String userId);
}
