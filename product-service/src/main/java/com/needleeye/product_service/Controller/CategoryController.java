package com.needleeye.product_service.Controller;

import com.needleeye.product_service.Dto.Request.CategoryDto;
import com.needleeye.product_service.Dto.Response.ApiResponse;
import com.needleeye.product_service.Service.CategoryService;
import com.needleeye.product_service.Utils.Constants.AppConstants;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping()
    public ResponseEntity<ApiResponse<?>> getAllCategories() {
        try {
            return categoryService.getAllCategories();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.SERVER_ERROR));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getCategoryById(@PathVariable Long id) {
        try {
            return categoryService.getCategoryById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.SERVER_ERROR));
    }

    @PostMapping()
    public ResponseEntity<ApiResponse<?>> addCategory(@Valid @RequestBody CategoryDto categoryData) {
        try {
           return categoryService.addCategory(categoryData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.SERVER_ERROR));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryDto categoryData) {
        try {
            return categoryService.updateCategory(id, categoryData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.SERVER_ERROR));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteCategory(@PathVariable Long id) {
        try {
            return categoryService.deleteCategory(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.SERVER_ERROR));
    }
}
