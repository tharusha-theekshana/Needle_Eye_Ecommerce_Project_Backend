package com.needleeye.product_service.Service;

import com.needleeye.product_service.Dto.Request.ReviewDto;
import com.needleeye.product_service.Dto.Response.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface ReviewService {
    ResponseEntity<ApiResponse<?>> getReviewsByProductId(String productId);
    ResponseEntity<ApiResponse<?>> getReviewsByUserId(String userId);
    ResponseEntity<ApiResponse<?>> addReview(String productId, ReviewDto reviewData);
}
