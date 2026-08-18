package com.kanishkaa.purchase_order.accounting_master.dto.tally_json;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;

public record AccountingMasterWrapper(

        @JsonProperty("Customer Master")
        List<AccountingMasterInnerDto> accountingMasters
) {

    public List<AccountingMasterInnerDto> getAccountingMasters() {
        if (accountingMasters == null || accountingMasters.isEmpty()) {
            return Collections.emptyList();
        }
        return accountingMasters;
    }
}
