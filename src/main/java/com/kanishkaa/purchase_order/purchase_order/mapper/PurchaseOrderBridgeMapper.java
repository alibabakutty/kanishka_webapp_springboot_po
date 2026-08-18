package com.kanishkaa.purchase_order.purchase_order.mapper;

import com.kanishkaa.purchase_order.purchase_order.dto.tally_json.PurchaseOrderInnerDto;
import com.kanishkaa.purchase_order.purchase_order.dto.tally_json.PurchaseOrderItemRawDto;
import com.kanishkaa.purchase_order.purchase_order.dto.api_side.PurchaseOrderRequest;
import com.kanishkaa.purchase_order.purchase_order.dto.api_side.PurchaseOrderSubFormRequest;
import com.kanishkaa.purchase_order.purchase_order.util.DateUtil;
import java.math.BigDecimal;
import java.util.List;

public class PurchaseOrderBridgeMapper {
    public static PurchaseOrderRequest toRequest(PurchaseOrderInnerDto dto) {
        if (dto == null) {
            throw new RuntimeException("PurchaseOrderInnerDto is null");
        }

        return new PurchaseOrderRequest(
                dto.voucherType(),
                DateUtil.parseDate(dto.voucherDate()),
                dto.voucherNumber(),
                dto.partyLedgerName(),
                dto.orderNo(),
                dto.totalAmount(),
                dto.narration(),
                dto.createdBy(),
                dto.approvedBy(),
                dto.companyName(),
                mapItems(dto.inventoryEntries())
        );
    }

    private static List<PurchaseOrderSubFormRequest> mapItems(List<PurchaseOrderItemRawDto> items) {
        if (items == null || items.isEmpty()) return List.of();

        return items.stream().map(item -> new PurchaseOrderSubFormRequest(
                item.itemName(),
                item.hsnCode(),
                item.gstPercentage(),
                item.itemUom(),
                item.companyName(),
                parseQty(item.billedQty()),
                parseDecimal(item.itemRate()),
                parseDecimal(item.itemAmount())
        )).toList();
    }

//    private static BigDecimal toBigDecimal(String value) {
//        if (value == null || value.isBlank()) return BigDecimal.ZERO;
//        return new BigDecimal(value);
//    }

    private static BigDecimal parseQty(String qty) {

        if (qty == null || qty.isBlank()) {
            return BigDecimal.ZERO;
        }

        try {

            String numericPart =
                    qty.trim()
                            .split("\\s+")[0];

            return new BigDecimal(numericPart);

        } catch (Exception e) {

            System.out.println(
                    "⚠ Invalid quantity received from Tally: " + qty
            );

            return BigDecimal.ZERO;
        }
    }

    private static BigDecimal parseDecimal(String value) {

        if (value == null || value.isBlank()) {
            return BigDecimal.ZERO;
        }

        try {
            return new BigDecimal(value.trim());
        } catch (Exception e) {

            System.out.println(
                    "⚠ Invalid decimal received from Tally: " + value
            );

            return BigDecimal.ZERO;
        }
    }
}
