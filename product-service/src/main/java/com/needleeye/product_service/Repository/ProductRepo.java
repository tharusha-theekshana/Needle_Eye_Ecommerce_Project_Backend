package com.needleeye.product_service.Repository;

import com.needleeye.product_service.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepo extends JpaRepository<Product,Long> {
    boolean existsByProductId(String productId);
    Optional<Product> findByProductId(String productId);
    List<Product> findByCreatedAtGreaterThanEqualOrderByCreatedAtDesc(LocalDate fromDate);
}
