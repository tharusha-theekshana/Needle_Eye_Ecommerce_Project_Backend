package com.needleeye.product_service.Repository;

import com.needleeye.product_service.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepo extends JpaRepository<Product,Long> {
    boolean existsByProductId(String productId);
}
