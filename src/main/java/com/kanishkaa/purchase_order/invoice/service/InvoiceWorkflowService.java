package com.kanishkaa.purchase_order.invoice.service;

import com.kanishkaa.purchase_order.invoice.dto.InvoiceRequest;
import com.kanishkaa.purchase_order.invoice.dto.Item;
import com.kanishkaa.purchase_order.purchase_order.dto.response_side.PurchaseOrderResponse;
import com.kanishkaa.purchase_order.purchase_order.service.PurchaseOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InvoiceWorkflowService {

    private final PurchaseOrderService poService;

    /**
     * Converts a Purchase Order into an InvoiceRequest pre-filled with items
     */
    public InvoiceRequest generateInvoiceRequestFromPO(Long poId, String invoiceNo, String customAddress) {
        // 1. Fetch PO data (which now includes our dynamic inventoryItemCount!)
        PurchaseOrderResponse po = poService.getById(poId);

        InvoiceRequest invoiceRequest = new InvoiceRequest();
        invoiceRequest.setInvoiceNo(invoiceNo);
        invoiceRequest.setDate(po.voucherDate().toString());
        invoiceRequest.setCustomerName(po.partyLedgerName());
        invoiceRequest.setOrderNo(po.orderNo());
        invoiceRequest.setAddress(customAddress != null ? customAddress : "No Address Provided");

        // 2. Map PurchaseOrderSubFormResponse list to Invoice Item DTO list
        List<Item> invoiceItems = po.inventoryEntries().stream().map(poItem -> {
            Item item = new Item();
            item.setName(poItem.itemName());
            item.setHsn(poItem.hsnCode());
            item.setGst(poItem.gstPercentage());

            // Handle conversions safely (BigDecimal to Integer for Qty)
            item.setQty(poItem.billedQty() != null ? poItem.billedQty().intValue() : 0);
            item.setRate(poItem.itemRate());
            item.setUom(poItem.itemUom());
            // itemAmount coming in from Tally might be negative or raw, we recalculate cleanly
            if (poItem.itemRate() != null && poItem.billedQty() != null) {
                item.setAmount(poItem.itemRate().multiply(poItem.billedQty()));
            } else {
                item.setAmount(BigDecimal.ZERO);
            }
            return item;
        }).toList();

        invoiceRequest.setItems(invoiceItems);
        return invoiceRequest;
    }
}
