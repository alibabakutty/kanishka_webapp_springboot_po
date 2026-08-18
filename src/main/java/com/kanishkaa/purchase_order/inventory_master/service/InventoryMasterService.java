package com.kanishkaa.purchase_order.inventory_master.service;

import com.kanishkaa.purchase_order.inventory_master.dto.api_side.InventoryMasterRequest;
import com.kanishkaa.purchase_order.inventory_master.dto.response_side.InventoryMasterResponse;
import com.kanishkaa.purchase_order.inventory_master.dto.tally_json.InventoryMasterWrapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface InventoryMasterService {
    @Transactional
    InventoryMasterResponse createInventoryMaster(InventoryMasterRequest request);

    @Transactional
    void saveAllFromTally(InventoryMasterWrapper wrapper);

    InventoryMasterResponse getByIdInventoryMaster(Long id);

    List<InventoryMasterResponse> getAllInventoryMasters();

    InventoryMasterResponse updateInventoryMaster(Long id, InventoryMasterRequest request);

    @Transactional
    void deleteByIdInventoryMaster(Long id);
}
