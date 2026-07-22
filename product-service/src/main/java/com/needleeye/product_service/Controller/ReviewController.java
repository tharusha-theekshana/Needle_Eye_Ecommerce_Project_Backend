package com.needleeye.product_service.Controller;

import com.needleeye.product_service.Dto.Request.ColorDto;
import com.needleeye.product_service.Dto.Request.ReviewApprovalDto;
import com.needleeye.product_service.Dto.Request.ReviewDto;
import com.needleeye.product_service.Dto.Response.ApiResponse;
import com.needleeye.product_service.Service.ReviewService;
import com.needleeye.product_service.Utils.Constants.AppConstants;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/review")
public class ReviewController {

    private ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<?>> getReviewsByProductId(@PathVariable String productId) {
        try {
            return reviewService.getReviewsByProductId(productId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.SERVER_ERROR));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<?>> getReviewsByUserId(@PathVariable String userId) {
        try {
            return reviewService.getReviewsByUserId(userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.SERVER_ERROR));
    }

    @PostMapping("/{productId}")
    public ResponseEntity<ApiResponse<?>> addReview(@PathVariable String productId, @Valid @RequestBody ReviewDto reviewData) {
        try {
            return reviewService.addReview(productId,reviewData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.SERVER_ERROR));
    }

    @PutMapping("/{reviewId}/approve")
    public ResponseEntity<ApiResponse<?>> updateReviewApprovalStatus(@PathVariable String reviewId, @Valid @RequestBody ReviewApprovalDto approvalData) {
        try {
            return reviewService.updateReviewApprovalStatus(reviewId, approvalData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.SERVER_ERROR));
    }
}
