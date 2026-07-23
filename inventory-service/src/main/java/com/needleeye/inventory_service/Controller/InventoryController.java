package com.needleeye.inventory_service.Controller;

import com.needleeye.inventory_service.Dto.Request.InventoryUpdateDto;
import com.needleeye.inventory_service.Dto.Response.ApiResponse;
import com.needleeye.inventory_service.Service.InventoryService;
import com.needleeye.inventory_service.Utils.Constants.AppConstants;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/inventory")
public class InventoryController {
    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping()
    public ResponseEntity<ApiResponse<?>> getAllInventories() {
        try {
            return inventoryService.getAllInventories();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.SERVER_ERROR));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<?>> getInventoryByProductId(
            @PathVariable String productId) {
        try {
            return inventoryService.getInventoryByProductId(productId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.SERVER_ERROR));
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ApiResponse<?>> updateInventory(
            @PathVariable String productId,
            @RequestBody @Valid InventoryUpdateDto inventoryData) {
        try {
            return inventoryService.updateInventory(productId, inventoryData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.SERVER_ERROR));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse<?>> deleteInventoryByProductId(
            @PathVariable String productId) {
        try {
            return inventoryService.deleteInventoryByProductId(productId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.SERVER_ERROR));
    }
}
