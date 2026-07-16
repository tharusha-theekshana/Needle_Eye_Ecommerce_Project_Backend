package com.needleeye.inventory_service.Service.Impl;

import com.needleeye.inventory_service.Entity.Inventory;
import com.needleeye.inventory_service.Repository.InventoryRepo;
import com.needleeye.inventory_service.Service.InventoryService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class InventoryServiceImpl implements InventoryService {
    private InventoryRepo inventoryRepo;

    public InventoryServiceImpl(InventoryRepo inventoryRepo) {
        this.inventoryRepo = inventoryRepo;
    }

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
}
