package com.needleeye.product_service.Service;

import com.needleeye.product_service.Dto.Request.SubCategoryDto;
import com.needleeye.product_service.Dto.Response.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface SubCategoryService {
    ResponseEntity<ApiResponse<?>> getAllSubCategories();
    ResponseEntity<ApiResponse<?>> getSubCategoryById(Long id);
    ResponseEntity<ApiResponse<?>> getSubCategoriesByCategoryId(Long categoryId);
    ResponseEntity<ApiResponse<?>> addSubCategory(SubCategoryDto subCategoryData);
    ResponseEntity<ApiResponse<?>> updateSubCategory(Long id, SubCategoryDto subCategoryData);
    ResponseEntity<ApiResponse<?>> deleteSubCategory(Long id);
}
