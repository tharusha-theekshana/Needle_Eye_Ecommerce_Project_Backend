package com.needleeye.product_service.Service.Impl;

import com.needleeye.product_service.Dto.Request.SubCategoryDto;
import com.needleeye.product_service.Dto.Response.ApiResponse;
import com.needleeye.product_service.Dto.Response.SubCategoryResponseDto;
import com.needleeye.product_service.Entity.Category;
import com.needleeye.product_service.Entity.SubCategory;
import com.needleeye.product_service.Repository.CategoryRepo;
import com.needleeye.product_service.Repository.SubCategoryRepo;
import com.needleeye.product_service.Service.SubCategoryService;
import com.needleeye.product_service.Utils.Constants.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SubCategoryServiceImpl implements SubCategoryService{
    private final SubCategoryRepo subCategoryRepo;
    private final CategoryRepo categoryRepo;

    public SubCategoryServiceImpl(SubCategoryRepo subCategoryRepo, CategoryRepo categoryRepo) {
        this.subCategoryRepo = subCategoryRepo;
        this.categoryRepo = categoryRepo;
    }

    // Get all sub categories
    @Override
    public ResponseEntity<ApiResponse<?>> getAllSubCategories() {
        try {
            List<SubCategoryResponseDto> subCategories = subCategoryRepo.findAll()
                    .stream()
                    .map(this::mapEntityToDto)
                    .collect(Collectors.toList());

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ApiResponse<>(HttpStatus.OK.value(), AppConstants.SUB_CATEGORIES_FETCHED, subCategories));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.SERVER_ERROR));
    }

    // Get sub category by id
    @Override
    public ResponseEntity<ApiResponse<?>> getSubCategoryById(Long id) {
        try {
            Optional<SubCategory> optionalSubCategory = subCategoryRepo.findById(id);

            if (optionalSubCategory.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), AppConstants.SUB_CATEGORY_NOT_FOUND));
            }

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ApiResponse<>(HttpStatus.OK.value(), AppConstants.SUB_CATEGORY_FETCHED, mapEntityToDto(optionalSubCategory.get())));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.SERVER_ERROR));
    }

    // Get sub categories by category id
    @Override
    public ResponseEntity<ApiResponse<?>> getSubCategoriesByCategoryId(Long categoryId) {
        try {
            Optional<Category> category = categoryRepo.findById(categoryId);

            if (category.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), AppConstants.CATEGORY_NOT_FOUND));
            }

            List<SubCategoryResponseDto> subCategories = subCategoryRepo.findByCategory_Id(categoryId)
                    .stream()
                    .map(this::mapEntityToDto)
                    .collect(Collectors.toList());

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ApiResponse<>(HttpStatus.OK.value(), AppConstants.SUB_CATEGORIES_FETCHED, subCategories));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.SERVER_ERROR));
    }

    // Add sub category
    @Override
    public ResponseEntity<ApiResponse<?>> addSubCategory(SubCategoryDto subCategoryData) {
        try {
            Optional<Category> category = categoryRepo.findById(subCategoryData.getCategoryId());

            if (category.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), AppConstants.CATEGORY_NOT_FOUND));
            }

            SubCategory mappedSubCategory = mapDtoToEntity(subCategoryData, category.get());
            subCategoryRepo.save(mappedSubCategory);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(HttpStatus.CREATED.value(), AppConstants.SUB_CATEGORY_ADDED));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.SERVER_ERROR));
    }

    // Update sub category
    @Override
    public ResponseEntity<ApiResponse<?>> updateSubCategory(Long id, SubCategoryDto subCategoryData) {
        try {
            Optional<SubCategory> optionalSubCategory = subCategoryRepo.findById(id);

            if (optionalSubCategory.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), AppConstants.SUB_CATEGORY_NOT_FOUND));
            }

            Optional<Category> category = categoryRepo.findById(subCategoryData.getCategoryId());

            if (category.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), AppConstants.CATEGORY_NOT_FOUND));
            }

            SubCategory existingSubCategory = optionalSubCategory.get();
            existingSubCategory.setSubCategory(subCategoryData.getSubCategory());
            existingSubCategory.setDescription(subCategoryData.getDescription());
            existingSubCategory.setCategory(category.get());
            existingSubCategory.setUpdatedAt(LocalDateTime.now());
            subCategoryRepo.save(existingSubCategory);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ApiResponse<>(HttpStatus.OK.value(), AppConstants.SUB_CATEGORY_UPDATED));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.SERVER_ERROR));
    }

    // Delete sub category
    @Override
    public ResponseEntity<ApiResponse<?>> deleteSubCategory(Long id) {
        try {
            Optional<SubCategory> optionalSubCategory = subCategoryRepo.findById(id);

            if (optionalSubCategory.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), AppConstants.SUB_CATEGORY_NOT_FOUND));
            }

            subCategoryRepo.deleteById(id);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ApiResponse<>(HttpStatus.OK.value(), AppConstants.SUB_CATEGORY_DELETED));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.SERVER_ERROR));
    }

    // Map DTO to entity
    private SubCategory mapDtoToEntity(SubCategoryDto subCategoryData, Category category) {
        SubCategory subCategory = new SubCategory();

        subCategory.setSubCategory(subCategoryData.getSubCategory());
        subCategory.setDescription(subCategoryData.getDescription());
        subCategory.setCategory(category);
        subCategory.setCreatedAt(LocalDateTime.now());
        subCategory.setUpdatedAt(LocalDateTime.now());

        return subCategory;
    }

    // Map entity to response DTO
    private SubCategoryResponseDto mapEntityToDto(SubCategory subCategory) {
        SubCategoryResponseDto dto = new SubCategoryResponseDto();

        dto.setId(subCategory.getId());
        dto.setSubCategory(subCategory.getSubCategory());
        dto.setDescription(subCategory.getDescription());
        dto.setCategoryId(subCategory.getCategory().getId());
        dto.setCategoryName(subCategory.getCategory().getCategory());
        dto.setCreatedAt(subCategory.getCreatedAt());
        dto.setUpdatedAt(subCategory.getUpdatedAt());

        return dto;
    }
}
