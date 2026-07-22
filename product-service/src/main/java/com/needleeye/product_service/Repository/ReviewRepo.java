package com.needleeye.product_service.Repository;

import com.needleeye.product_service.Entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepo extends JpaRepository<Review,Long> {
    boolean existsByReviewId(String reviewId);
    List<Review> findByProductId(String productId);
    List<Review> findByUserId(String userId);
}
