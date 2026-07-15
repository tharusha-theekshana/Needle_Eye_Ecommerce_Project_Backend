package com.needleeye.product_service.Dto.Request;

import com.needleeye.product_service.Utils.Enums.SizeType;
import jakarta.validation.constraints.*;

import java.util.List;

public class ProductDto {

    @NotBlank(message = "Image URL is required")
    private String imageUrl;

    @NotBlank(message = "Product name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Description is required")
    @Size(min = 10, max = 2000, message = "Description must be between 10 and 2000 characters")
    private String description;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be a positive number")
    @DecimalMin(value = "0.01", message = "Price must be at least 0.01")
    private Double price;

    @Min(value = 0, message = "Discount cannot be negative")
    @Max(value = 100, message = "Discount cannot exceed 100%")
    private Double discountPercentage = 0.0;

    @NotNull(message = "Availability status is required")
    private Boolean isAvailable;

    @NotNull(message = "Category ID is required")
    private Long categoryId;

    @NotEmpty(message = "At least one color must be selected")
    private List<Long> colorIds;

    @NotEmpty(message = "At least one size must be selected")
    private List<SizeType> sizes;

    public ProductDto() {
    }

    public ProductDto(String imageUrl, String name, String description, @NotNull(message = "Price is required") Double price, Double discountPercentage, @NotNull(message = "Availability status is required") Boolean isAvailable, @NotNull(message = "Category ID is required") Long categoryId, List<Long> colorIds, List<SizeType> sizes) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.description = description;
        this.price = price;
        this.discountPercentage = discountPercentage;
        this.isAvailable = isAvailable;
        this.categoryId = categoryId;
        this.colorIds = colorIds;
        this.sizes = sizes;
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

    public Boolean getAvailable() {
        return isAvailable;
    }

    public void setAvailable(Boolean available) {
        isAvailable = available;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public List<Long> getColorIds() {
        return colorIds;
    }

    public void setColorIds(List<Long> colorIds) {
        this.colorIds = colorIds;
    }

    public List<SizeType> getSizes() {
        return sizes;
    }

    public void setSizes(List<SizeType> sizes) {
        this.sizes = sizes;
    }
}
