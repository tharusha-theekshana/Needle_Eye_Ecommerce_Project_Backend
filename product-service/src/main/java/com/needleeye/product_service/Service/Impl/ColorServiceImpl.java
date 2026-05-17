package com.needleeye.product_service.Service.Impl;

import com.needleeye.product_service.Dto.Request.ColorDto;
import com.needleeye.product_service.Dto.Response.ApiResponse;
import com.needleeye.product_service.Entity.Color;
import com.needleeye.product_service.Repository.ColorRepo;
import com.needleeye.product_service.Service.ColorService;
import com.needleeye.product_service.Utils.Constants.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ColorServiceImpl implements ColorService {

    private ColorRepo colorRepo;

    public ColorServiceImpl(ColorRepo colorRepo) {
        this.colorRepo = colorRepo;
    }

    @Override
    public ResponseEntity<ApiResponse<?>> getAllColors() {
        try {
            List<Color> colors = colorRepo.findAll();
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ApiResponse<>(HttpStatus.OK.value(), AppConstants.COLORS_FETCHED, colors));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.SERVER_ERROR));
    }

    @Override
    public ResponseEntity<ApiResponse<?>> getColorById(Long id) {
        try {
            Color color = colorRepo.findById(id).orElse(null);

            if (color == null) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), AppConstants.COLOR_NOT_FOUND));
            }

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ApiResponse<>(HttpStatus.OK.value(), AppConstants.COLOR_FETCHED, color));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.SERVER_ERROR));
    }

    @Override
    public ResponseEntity<ApiResponse<?>> addColors(ColorDto colorData) {
        try {
            Color color = new Color();
            color.setColor(colorData.getColor());
            color.setColorCode(colorData.getColorCode());
            color.setCreatedAt(LocalDateTime.now());
            colorRepo.save(color);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ApiResponse<>(HttpStatus.OK.value(), AppConstants.COLOR_ADDED));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.SERVER_ERROR));
    }

    @Override
    public ResponseEntity<ApiResponse<?>> updateColor(Long id, ColorDto colorData) {
        try {
            Color existingColor = colorRepo.findById(id).orElse(null);

            if (existingColor == null) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), AppConstants.COLOR_NOT_FOUND));
            }

            existingColor.setColor(colorData.getColor());
            existingColor.setColorCode(colorData.getColorCode());
            colorRepo.save(existingColor);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ApiResponse<>(HttpStatus.OK.value(), AppConstants.COLOR_UPDATED));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.SERVER_ERROR));
    }

    @Override
    public ResponseEntity<ApiResponse<?>> deleteColor(Long id) {
        try {
            Color existingColor = colorRepo.findById(id).orElse(null);

            if (existingColor == null) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), AppConstants.COLOR_NOT_FOUND));
            }
            colorRepo.deleteById(id);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ApiResponse<>(HttpStatus.OK.value(), AppConstants.COLOR_DELETED));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.SERVER_ERROR));
    }
}
