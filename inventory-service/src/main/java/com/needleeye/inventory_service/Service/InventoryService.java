package com.needleeye.inventory_service.Service;

import com.needleeye.inventory_service.Dto.Request.InventoryUpdateDto;
import com.needleeye.inventory_service.Dto.Response.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface InventoryService {
    void createInventoryForProduct(String productId);
    ResponseEntity<ApiResponse<?>> getAllInventories();
    ResponseEntity<ApiResponse<?>> getInventoryByProductId(String productId);
    ResponseEntity<ApiResponse<?>> updateInventory(String productId, InventoryUpdateDto inventoryData);
    ResponseEntity<ApiResponse<?>> deleteInventoryByProductId(String productId);
}
