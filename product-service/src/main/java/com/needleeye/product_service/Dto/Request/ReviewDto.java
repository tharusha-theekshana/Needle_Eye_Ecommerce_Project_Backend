package com.needleeye.product_service.Dto.Request;

import jakarta.validation.constraints.*;

public class ReviewDto {
    @NotBlank(message = "User id is required")
    private String userId;

    @NotBlank(message = "Comment is required")
    private String comment;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must not exceed 5")
    private Integer rating;

    public ReviewDto() {
    }

    public ReviewDto(String userId, String comment, @NotNull(message = "Rating is required") Integer rating) {
        this.userId = userId;
        this.comment = comment;
        this.rating = rating;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
