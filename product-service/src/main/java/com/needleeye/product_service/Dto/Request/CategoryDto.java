package com.needleeye.product_service.Dto.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class CategoryDto {

    @NotBlank(message = "Category name is required")
    private String category;

    @NotBlank(message = "Description is required")
    private String description;

    public CategoryDto() {
    }

    public CategoryDto(String category, String description) {
        this.category = category;
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
