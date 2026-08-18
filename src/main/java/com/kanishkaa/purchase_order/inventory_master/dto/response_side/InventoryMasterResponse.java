package com.kanishkaa.purchase_order.inventory_master.dto.response_side;

import java.math.BigDecimal;

public record InventoryMasterResponse(
        Long id,
        String itemName,
        String partNumber,
        String itemUom,
        String hsnCode,
        BigDecimal gstPercentage,
        BigDecimal itemRate
) {
}
