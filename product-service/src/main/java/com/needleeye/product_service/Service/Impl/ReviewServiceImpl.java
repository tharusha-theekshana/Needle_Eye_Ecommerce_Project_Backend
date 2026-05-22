package com.needleeye.product_service.Service.Impl;

import com.needleeye.product_service.Dto.Request.ReviewDto;
import com.needleeye.product_service.Dto.Response.ApiResponse;
import com.needleeye.product_service.Repository.ReviewRepo;
import com.needleeye.product_service.Service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ReviewServiceImpl implements ReviewService {

    private ReviewRepo reviewRepo;

    public ReviewServiceImpl(ReviewRepo reviewRepo) {
        this.reviewRepo = reviewRepo;
    }

    @Override
    public ResponseEntity<ApiResponse<?>> addReview(Long productId, ReviewDto reviewData) {
        return null;
    }
}
