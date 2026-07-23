package com.needleeye.product_service.Controller;

import com.needleeye.product_service.Dto.Request.ColorDto;
import com.needleeye.product_service.Dto.Response.ApiResponse;
import com.needleeye.product_service.Service.ColorService;
import com.needleeye.product_service.Utils.Constants.AppConstants;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/color")
public class ColorController {

    private final ColorService colorService;

    public ColorController(ColorService colorService) {
        this.colorService = colorService;
    }

    @GetMapping()
    public ResponseEntity<ApiResponse<?>> getAllColors() {
        try {
            return colorService.getAllColors();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.SERVER_ERROR));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getColorById(@PathVariable Long id) {
        try {
            return colorService.getColorById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.SERVER_ERROR));
    }

    @PostMapping()
    public ResponseEntity<ApiResponse<?>> addColor(@Valid @RequestBody ColorDto colorData) {
        try {
            return colorService.addColors(colorData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.SERVER_ERROR));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> updateColor(@PathVariable Long id, @Valid @RequestBody ColorDto colorData) {
        try {
            return colorService.updateColor(id, colorData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.SERVER_ERROR));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteColor(@PathVariable Long id) {
        try {
            return colorService.deleteColor(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.SERVER_ERROR));
    }
}
