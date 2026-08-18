package com.kanishkaa.purchase_order.inventory_master.repository;

import com.kanishkaa.purchase_order.inventory_master.model.InventoryMasterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface InventoryMasterRepository extends JpaRepository<InventoryMasterEntity, Long> {
    boolean existsByItemName(String itemName);
    Optional<InventoryMasterEntity> findByItemName(String itemName);
}
