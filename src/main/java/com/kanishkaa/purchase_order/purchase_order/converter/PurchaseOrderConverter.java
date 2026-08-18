package com.kanishkaa.purchase_order.purchase_order.converter;

import com.kanishkaa.purchase_order.purchase_order.dto.api_side.PurchaseOrderRequest;
import com.kanishkaa.purchase_order.purchase_order.dto.api_side.PurchaseOrderSubFormRequest;
import com.kanishkaa.purchase_order.purchase_order.dto.tally_json.PurchaseOrderWrapper;
import com.kanishkaa.purchase_order.purchase_order.dto.tally_xml.PurchaseOrderTallyXmlDto;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class PurchaseOrderConverter {
    // Tally JSON -> DTO
    public PurchaseOrderRequest fromTallyJson(PurchaseOrderWrapper wrapper) {

        var dto = wrapper.voucherDetails();

        List<PurchaseOrderSubFormRequest> items =
                dto.inventoryEntries()
                        .stream()
                        .map(item -> new PurchaseOrderSubFormRequest(
                                item.itemName(),
                                item.hsnCode(),
                                item.gstPercentage(),
                                item.itemUom(),
                                item.companyName(),
                                extractQty(item.billedQty()),
                                safeDecimal(item.itemRate()),
                                safeDecimal(item.itemAmount())
                        ))
                        .toList();

        return new PurchaseOrderRequest(
                dto.voucherType(),
                parseDate(dto.voucherDate()),
                dto.voucherNumber(),
                dto.partyLedgerName(),
                dto.orderNo(),
                dto.totalAmount(),
                dto.narration(),
                dto.createdBy(),
                dto.approvedBy(),
                dto.companyName(),
                items
        );
    }

    // XML -> DTO
    public PurchaseOrderRequest fromXml(PurchaseOrderTallyXmlDto xml) {

        List<PurchaseOrderSubFormRequest> items =
                xml.getInventoryEntries()
                        .stream()
                        .map(item -> new PurchaseOrderSubFormRequest(
                                item.getItemName(),
                                item.getHsnCode(),
                                item.getGstPercentage(),
                                item.getItemUom(),
                                item.getCompanyName(),
                                safeDecimal(item.getBilledQty()),
                                safeDecimal(item.getRate()),
                                safeDecimal(item.getAmount())
                        ))
                        .toList();

        return new PurchaseOrderRequest(
                xml.getVoucherType(),
                parseDate(xml.getVoucherDate()),
                xml.getVoucherNumber(),
                xml.getPartyLedgerName(),
                xml.getOrderNo(),
                xml.getTotalAmount(),
                xml.getCreatedBy(),
                xml.getApprovedBy(),
                xml.getNarration(),
                xml.getCompanyName(),
                items
        );
    }

    /**
     * Converts:
     * "6 box"      -> 6
     * "5 Nos"      -> 5
     * "5.000 Kgs"  -> 5.000
     * null         -> 0
     */

    private BigDecimal extractQty(String billedQty) {

        if (billedQty == null || billedQty.isBlank()) {
            return BigDecimal.ZERO;
        }

        try {

            String qty =
                    billedQty.trim()
                            .split("\\s+")[0];

            return new BigDecimal(qty);

        } catch (Exception e) {

            System.out.println(
                    "⚠ Invalid billedQty from Tally: " + billedQty
            );

            return BigDecimal.ZERO;
        }
    }

    private BigDecimal safeDecimal(String value) {

        if (value == null || value.isBlank()) {
            return BigDecimal.ZERO;
        }

        try {
            return new BigDecimal(value.trim());
        } catch (Exception e) {

            System.out.println(
                    "⚠ Invalid decimal from Tally: " + value
            );

            return BigDecimal.ZERO;
        }
    }

    private LocalDate parseDate(String date) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern("d-M-yyyy"));
    }
}
