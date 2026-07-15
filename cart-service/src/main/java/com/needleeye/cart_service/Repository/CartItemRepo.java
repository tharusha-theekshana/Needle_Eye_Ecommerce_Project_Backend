package com.needleeye.cart_service.Repository;

import com.needleeye.cart_service.Entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepo extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCartIdAndProductIdAndSizeAndColor(Long cartId, String productId, String size, String color);
    Optional<CartItem> findByIdAndCartId(Long id, Long cartId);
}
