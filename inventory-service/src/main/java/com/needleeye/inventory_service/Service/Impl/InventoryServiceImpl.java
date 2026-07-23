package com.needleeye.inventory_service.Service.Impl;

import com.needleeye.inventory_service.Dto.Request.InventoryUpdateDto;
import com.needleeye.inventory_service.Dto.Response.ApiResponse;
import com.needleeye.inventory_service.Entity.Inventory;
import com.needleeye.inventory_service.Repository.InventoryRepo;
import com.needleeye.inventory_service.Service.InventoryService;
import com.needleeye.inventory_service.Utils.Constants.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class InventoryServiceImpl implements InventoryService {
    private final InventoryRepo inventoryRepo;

    public InventoryServiceImpl(InventoryRepo inventoryRepo) {
        this.inventoryRepo = inventoryRepo;
    }

    // Set inventory
    @Override
    public void createInventoryForProduct(String productId) {
        try {

            // Check already has inventory for product
            if (inventoryRepo.existsByProductId(productId)) {
                return;
            }

            Inventory inventory = new Inventory();
            inventory.setProductId(productId);
            inventory.setTotalInventory(1);
            inventory.setAvailable(1);
            inventory.setSell(0);
            inventory.setCreatedAt(LocalDate.now());
            inventory.setUpdatedAt(LocalDate.now());

            inventoryRepo.save(inventory);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Get all inventories
    @Override
    public ResponseEntity<ApiResponse<?>> getAllInventories() {
        try {
            List<Inventory> inventories = inventoryRepo.findAll();

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ApiResponse<>(HttpStatus.OK.value(), AppConstants.INVENTORIES_FETCHED, inventories));

        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.SERVER_ERROR));
    }

    // Get inventory by product id
    @Override
    public ResponseEntity<ApiResponse<?>> getInventoryByProductId(String productId) {
        try {
            Optional<Inventory> optionalInventory = inventoryRepo.findByProductId(productId);

            if(optionalInventory.isEmpty()){
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), AppConstants.INVENTORY_NOT_FOUND));
            }

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ApiResponse<>(HttpStatus.OK.value(), AppConstants.INVENTORY_FETCHED, optionalInventory.get()));

        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.SERVER_ERROR));
    }

    // Update inventory
    @Override
    public ResponseEntity<ApiResponse<?>> updateInventory(String productId, InventoryUpdateDto inventoryData) {
        try {

            Optional<Inventory> optionalInventory = inventoryRepo.findByProductId(productId);

            if (optionalInventory.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), AppConstants.INVENTORY_NOT_FOUND));
            }

            if (inventoryData.getAvailable() + inventoryData.getSell() > inventoryData.getTotalInventory()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), AppConstants.INVALID_INVENTORY_DATA));
            }

            Inventory inventory = optionalInventory.get();
            inventory.setTotalInventory(inventoryData.getTotalInventory());
            inventory.setAvailable(inventoryData.getAvailable());
            inventory.setSell(inventoryData.getSell());
            inventory.setUpdatedAt(LocalDate.now());

            inventoryRepo.save(inventory);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(HttpStatus.CREATED.value(), AppConstants.INVENTORY_UPDATED, inventory));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.SERVER_ERROR));
    }

    // Delete inventory
    @Override
    public ResponseEntity<ApiResponse<?>> deleteInventoryByProductId(String productId) {
        try {
            Optional<Inventory> optionalInventory = inventoryRepo.findByProductId(productId);

            if(optionalInventory.isEmpty()){
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), AppConstants.INVENTORY_NOT_FOUND));
            }

            inventoryRepo.delete(optionalInventory.get());
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ApiResponse<>(HttpStatus.OK.value(), AppConstants.INVENTORY_DELETED));

        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.SERVER_ERROR));
    }
}
