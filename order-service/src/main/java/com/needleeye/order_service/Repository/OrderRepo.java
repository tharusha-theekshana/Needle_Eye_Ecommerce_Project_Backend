package com.needleeye.order_service.Repository;

import com.needleeye.order_service.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepo extends JpaRepository<Order,Long> {
    boolean existsByOrderId(String orderId);
}
