package com.needleeye.product_service.Dto.Response;

import com.needleeye.product_service.Entity.Review;
import com.needleeye.product_service.Utils.Enums.SizeType;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

public class ProductResponseDto {
    private Long id;
    private String productId;
    private String imageUrl;
    private String name;
    private String description;
    private Double price;
    private Double discountPercentage;
    private Double lastPrice;
    private Boolean isAvailable;
    private String categoryName;
    private List<String> colorCodes;
    private List<SizeType> sizes;
    private List<Review> reviews;
    private Double averageRating;
    private Integer totalReviews;
    private HashMap<String, Integer> inventory;
    private LocalDate createdAt;
    private LocalDate updatedAt;

    public ProductResponseDto() {
    }

    public ProductResponseDto(Long id, String productId, String imageUrl, String name, String description, Double price, Double discountPercentage, Double lastPrice, Boolean isAvailable, String categoryName, List<String> colorCodes, List<SizeType> sizes, List<Review> reviews, Double averageRating, Integer totalReviews, HashMap<String, Integer> inventory, LocalDate createdAt, LocalDate updatedAt) {
        this.id = id;
        this.productId = productId;
        this.imageUrl = imageUrl;
        this.name = name;
        this.description = description;
        this.price = price;
        this.discountPercentage = discountPercentage;
        this.lastPrice = lastPrice;
        this.isAvailable = isAvailable;
        this.categoryName = categoryName;
        this.colorCodes = colorCodes;
        this.sizes = sizes;
        this.reviews = reviews;
        this.averageRating = averageRating;
        this.totalReviews = totalReviews;
        this.inventory = inventory;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(Double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public Double getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(Double lastPrice) {
        this.lastPrice = lastPrice;
    }

    public Boolean getAvailable() {
        return isAvailable;
    }

    public void setAvailable(Boolean available) {
        isAvailable = available;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<String> getColorCodes() {
        return colorCodes;
    }

    public void setColorCodes(List<String> colorCodes) {
        this.colorCodes = colorCodes;
    }

    public List<SizeType> getSizes() {
        return sizes;
    }

    public void setSizes(List<SizeType> sizes) {
        this.sizes = sizes;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    public Integer getTotalReviews() {
        return totalReviews;
    }

    public void setTotalReviews(Integer totalReviews) {
        this.totalReviews = totalReviews;
    }

    public HashMap<String, Integer> getInventory() {
        return inventory;
    }

    public void setInventory(HashMap<String, Integer> inventory) {
        this.inventory = inventory;
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
