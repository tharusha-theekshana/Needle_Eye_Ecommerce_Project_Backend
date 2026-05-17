package com.needleeye.product_service.Service;

import com.needleeye.product_service.Dto.Request.CategoryDto;
import com.needleeye.product_service.Dto.Response.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface CategoryService {
    ResponseEntity<ApiResponse<?>> getAllCategories();
    ResponseEntity<ApiResponse<?>> getCategoryById(Long id);
    ResponseEntity<ApiResponse<?>> addCategory(CategoryDto categoryData);
    ResponseEntity<ApiResponse<?>> updateCategory(Long id, CategoryDto categoryData);
    ResponseEntity<ApiResponse<?>> deleteCategory(Long id);

}
