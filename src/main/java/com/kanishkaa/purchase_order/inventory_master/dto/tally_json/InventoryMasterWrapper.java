package com.kanishkaa.purchase_order.inventory_master.dto.tally_json;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collections;
import java.util.List;

public record InventoryMasterWrapper(
        @JsonProperty("Inventory Master")
        List<InventoryMasterInnerDto> inventoryMasters
) {
    public List<InventoryMasterInnerDto> getInventoryMasters() {
        if (inventoryMasters == null || inventoryMasters.isEmpty()) {
            return Collections.emptyList();
        }
        return inventoryMasters;
    }
}
