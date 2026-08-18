package com.kanishkaa.purchase_order.accounting_master.dto.api_side;

import jakarta.validation.constraints.NotBlank;

public record AccountingMasterRequest(
        @NotBlank String sundryCreditorName,
        String phoneNumber,
        String mailId,
        String panNumber,
        String gstNumber,
        String parentName,
        String grandParentName
) {
}
