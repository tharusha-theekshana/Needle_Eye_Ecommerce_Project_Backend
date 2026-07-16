package com.needleeye.inventory_service.Repository;

import com.needleeye.inventory_service.Entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepo extends JpaRepository<Inventory,Long> {
    boolean existsByProductId(String productId);
}
