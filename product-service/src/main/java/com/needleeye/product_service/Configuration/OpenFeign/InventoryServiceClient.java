package com.needleeye.product_service.Configuration.OpenFeign;

import com.needleeye.product_service.Dto.Response.ApiResponse;
import com.needleeye.product_service.Dto.Response.InventoryResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "INVENTORY-SERVICE")
public interface InventoryServiceClient {

    @GetMapping("/api/v1/inventory/{productId}")
    ResponseEntity<ApiResponse<InventoryResponseDto>> getInventoryByProductId(
            @PathVariable String productId);

    @GetMapping("/api/v1/inventory")
    ResponseEntity<ApiResponse<List<InventoryResponseDto>>> getAllInventories();
}
