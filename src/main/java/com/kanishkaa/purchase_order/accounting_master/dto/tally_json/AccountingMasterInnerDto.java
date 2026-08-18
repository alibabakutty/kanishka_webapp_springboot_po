package com.kanishkaa.purchase_order.accounting_master.dto.tally_json;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AccountingMasterInnerDto(
        @JsonProperty("name")
        String sundryCreditorName,

        @JsonProperty("MobileNo")
        String phoneNumber,

        @JsonProperty("email")
        String mailId,

        @JsonProperty("PanNo")
        String panNumber,

        @JsonProperty("GSTNo")
        String gstNumber,

        @JsonProperty("parent")
        String parentName,

        @JsonProperty("grandparent")
        String grandParentName
) {
}
