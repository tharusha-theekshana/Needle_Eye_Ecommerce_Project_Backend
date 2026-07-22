package com.needleeye.product_service.Service.Impl;

import com.needleeye.product_service.Configuration.OpenFeign.UserServiceClient;
import com.needleeye.product_service.Dto.Request.ReviewApprovalDto;
import com.needleeye.product_service.Dto.Request.ReviewDto;
import com.needleeye.product_service.Dto.Response.ApiResponse;
import com.needleeye.product_service.Dto.Response.UserResponseDto;
import com.needleeye.product_service.Entity.Product;
import com.needleeye.product_service.Entity.Review;
import com.needleeye.product_service.Repository.ProductRepo;
import com.needleeye.product_service.Repository.ReviewRepo;
import com.needleeye.product_service.Service.ReviewService;
import com.needleeye.product_service.Utils.Constants.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepo reviewRepo;
    private final ProductRepo productRepo;
    private final UserServiceClient userServiceClient;

    public ReviewServiceImpl(ReviewRepo reviewRepo, ProductRepo productRepo, UserServiceClient userServiceClient) {
        this.reviewRepo = reviewRepo;
        this.productRepo = productRepo;
        this.userServiceClient = userServiceClient;
    }

    // Get reviews by product id
    @Override
    public ResponseEntity<ApiResponse<?>> getReviewsByProductId(String productId) {
        try{
            List<Review> reviewList = reviewRepo.findByProductId(productId);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ApiResponse<>(HttpStatus.OK.value(), AppConstants.REVIEWS_FETCHED,reviewList));

        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.SERVER_ERROR));
    }

    // Get reviews by user id
    @Override
    public ResponseEntity<ApiResponse<?>> getReviewsByUserId(String userId) {
        try{
            List<Review> reviewList = reviewRepo.findByUserId(userId);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ApiResponse<>(HttpStatus.OK.value(), AppConstants.REVIEWS_FETCHED,reviewList));

        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.SERVER_ERROR));
    }

    // Add review
    @Override
    public ResponseEntity<ApiResponse<?>> addReview(String productId, ReviewDto reviewData) {
        try{
            Optional<Product> optionalProduct = productRepo.findByProductId(productId);

            if (optionalProduct.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), AppConstants.PRODUCT_NOT_FOUND));
            }

            // Get user details
            ResponseEntity<ApiResponse<UserResponseDto>> userResponseData = userServiceClient.getUserDataById(reviewData.getUserId());
            UserResponseDto userData = userResponseData.getBody().getData();

            if (userData == null) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), AppConstants.USER_NOT_FOUND));
            }

            Review review = mapDtoToEntity(reviewData,optionalProduct.get(), userData);
            reviewRepo.save(review);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(HttpStatus.CREATED.value(), AppConstants.REVIEW_ADDED, review));

        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.SERVER_ERROR));
    }

    // Update review approval status
    @Override
    public ResponseEntity<ApiResponse<?>> updateReviewApprovalStatus(String reviewId, ReviewApprovalDto approvalData) {
        try {
            Optional<Review> optionalReview = reviewRepo.findByReviewId(reviewId);

            if (optionalReview.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), AppConstants.REVIEW_NOT_FOUND));
            }

            Review review = optionalReview.get();
            review.setApproved(approvalData.getApproved());
            review.setUpdatedAt(LocalDate.now());

            reviewRepo.save(review);

            String message = Boolean.TRUE.equals(approvalData.getApproved())
                    ? AppConstants.REVIEW_APPROVED
                    : AppConstants.REVIEW_REJECTED;

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ApiResponse<>(HttpStatus.OK.value(), message, review));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.SERVER_ERROR));
    }

    // Map review data to entity
    private Review mapDtoToEntity(ReviewDto reviewData,Product product, UserResponseDto userData){
        Review review = new Review();

        review.setReviewId(generateUniqueReviewId());
        review.setProductId(product.getProductId());
        review.setUserId(reviewData.getUserId());
        review.setReviewerName((userData.getFirstName() + " " + userData.getLastName()).trim());
        review.setReviewerEmail(userData.getEmail());
        review.setComment(reviewData.getComment());
        review.setRating(reviewData.getRating());
        review.setApproved(false);
        review.setCreatedAt(LocalDate.now());
        review.setUpdatedAt(LocalDate.now());

        return review;
    }

    // Generate review id
    private String generateUniqueReviewId() {
        String reviewId;
        do {
            int number = new Random().nextInt(900000) + 100000;
            reviewId = "REV" + number;
        } while (reviewRepo.existsByReviewId(reviewId));
        return reviewId;
    }
}
