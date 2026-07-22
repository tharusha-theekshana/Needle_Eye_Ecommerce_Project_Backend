package com.needleeye.product_service.Controller;

import com.needleeye.product_service.Dto.Request.ColorDto;
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

}
