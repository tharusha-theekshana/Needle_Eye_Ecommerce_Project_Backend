package com.needleeye.product_service.Service.Impl;

import com.needleeye.product_service.Dto.Request.CategoryDto;
import com.needleeye.product_service.Dto.Response.ApiResponse;
import com.needleeye.product_service.Entity.Category;
import com.needleeye.product_service.Repository.CategoryRepo;
import com.needleeye.product_service.Service.CategoryService;
import com.needleeye.product_service.Utils.Constants.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepo categoryRepo;

    public CategoryServiceImpl(CategoryRepo categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    // Get all categories
    @Override
    public ResponseEntity<ApiResponse<?>> getAllCategories() {
        try {
            List<Category> categories = categoryRepo.findAll();
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ApiResponse<>(HttpStatus.OK.value(), AppConstants.CATEGORIES_FETCHED, categories));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.SERVER_ERROR));
    }

    // Get category by category id
    @Override
    public ResponseEntity<ApiResponse<?>> getCategoryById(Long id) {
        try {
            Category category = categoryRepo.findById(id).orElse(null);

            if (category == null) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), AppConstants.CATEGORY_NOT_FOUND));
            }

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ApiResponse<>(HttpStatus.OK.value(), AppConstants.CATEGORY_FETCHED, category));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.SERVER_ERROR));
    }

    // Add category
    @Override
    public ResponseEntity<ApiResponse<?>> addCategory(CategoryDto categoryData) {
        try {

            Category mappedCategory = mapDataToCategoryEntity(categoryData);
            categoryRepo.save(mappedCategory);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ApiResponse<>(HttpStatus.OK.value(), AppConstants.CATEGORY_ADDED));


        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.SERVER_ERROR));
    }

    // Update category
    @Override
    public ResponseEntity<ApiResponse<?>> updateCategory(Long id, CategoryDto categoryData) {
        try {
            Category existingCategory = categoryRepo.findById(id).orElse(null);

            if (existingCategory == null) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), AppConstants.CATEGORY_NOT_FOUND));
            }

            existingCategory.setCategory(categoryData.getCategory());
            existingCategory.setDescription(categoryData.getDescription());
            existingCategory.setUpdatedAt(LocalDateTime.now());
            categoryRepo.save(existingCategory);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ApiResponse<>(HttpStatus.OK.value(), AppConstants.CATEGORY_UPDATED));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.SERVER_ERROR));
    }

    // Delete category by id
    @Override
    public ResponseEntity<ApiResponse<?>> deleteCategory(Long id) {
        try {
            Category existingCategory = categoryRepo.findById(id).orElse(null);

            if (existingCategory == null) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), AppConstants.CATEGORY_NOT_FOUND));
            }

            categoryRepo.deleteById(id);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ApiResponse<>(HttpStatus.OK.value(), AppConstants.CATEGORY_DELETED));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.SERVER_ERROR));
    }


    // Map DTO to entity
    Category mapDataToCategoryEntity(CategoryDto categoryData) {
        Category category = new Category();

        category.setCategory(categoryData.getCategory());
        category.setDescription(categoryData.getDescription());
        category.setCreatedAt(LocalDateTime.now());
        category.setUpdatedAt(LocalDateTime.now());

        return category;
    }

}
