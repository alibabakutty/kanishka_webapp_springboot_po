package com.kanishkaa.purchase_order.inventory_master.dto.tally_json;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

public record InventoryMasterInnerDto(
        @JsonProperty("name")
        String itemName,

        @JsonProperty("itemno")
        String partNumber,

        @JsonProperty("baseunits")
        String itemUom,

        @JsonProperty("ItemHSN")
        String hsnCode,

        @JsonProperty("ItemGST")
        BigDecimal gstPercentage,

        @JsonProperty("ItemRate")
        BigDecimal itemRate


) {
}
