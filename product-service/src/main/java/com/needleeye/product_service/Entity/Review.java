package com.needleeye.product_service.Entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String reviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private String reviewerName;
    private String reviewerEmail;

    @Column(columnDefinition = "TEXT")
    private String comment;

    private Integer rating;

    private Boolean isApproved;

    private LocalDate createdAt;
    private LocalDate updatedAt;

    public Review() {
    }

    public Review(Long id, String reviewId, Product product, String reviewerName, String reviewerEmail, String comment, Integer rating, Boolean isApproved, LocalDate createdAt, LocalDate updatedAt) {
        this.id = id;
        this.reviewId = reviewId;
        this.product = product;
        this.reviewerName = reviewerName;
        this.reviewerEmail = reviewerEmail;
        this.comment = comment;
        this.rating = rating;
        this.isApproved = isApproved;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
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

    public Boolean getApproved() {
        return isApproved;
    }

    public void setApproved(Boolean approved) {
        isApproved = approved;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }
}
