package com.kanishkaa.purchase_order.accounting_master.dto.response_side;

public record AccountingMasterResponse(
        Long id,
        String sundryCreditorName,
        String phoneNumber,
        String mailId,
        String panNumber,
        String gstNumber,
        String parentName,
        String grandParentName
) {
}
