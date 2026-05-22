package com.needleeye.product_service.Dto.Request;

import jakarta.validation.constraints.*;

public class ReviewDto {
    @NotBlank(message = "Reviewer name is required")
    private String reviewerName;

    @NotBlank(message = "Reviewer email is required")
    @Email(message = "Invalid email format")
    private String reviewerEmail;

    @NotBlank(message = "Comment is required")
    private String comment;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must not exceed 5")
    private Integer rating;

    public ReviewDto() {
    }

    public ReviewDto(String reviewerName, String reviewerEmail, String comment, @NotNull(message = "Rating is required") Integer rating) {
        this.reviewerName = reviewerName;
        this.reviewerEmail = reviewerEmail;
        this.comment = comment;
        this.rating = rating;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

    public String getReviewerEmail() {
        return reviewerEmail;
    }

    public void setReviewerEmail(String reviewerEmail) {
        this.reviewerEmail = reviewerEmail;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
}
