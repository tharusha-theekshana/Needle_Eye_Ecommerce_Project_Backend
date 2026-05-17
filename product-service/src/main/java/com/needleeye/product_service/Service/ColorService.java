package com.needleeye.product_service.Service;

import com.needleeye.product_service.Dto.Request.ColorDto;
import com.needleeye.product_service.Dto.Response.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface ColorService {
    ResponseEntity<ApiResponse<?>> getAllColors();
    ResponseEntity<ApiResponse<?>> getColorById(Long id);
    ResponseEntity<ApiResponse<?>> addColors(ColorDto colorData);
    ResponseEntity<ApiResponse<?>> updateColor(Long id, ColorDto colorData);
    ResponseEntity<ApiResponse<?>> deleteColor(Long id);
}
