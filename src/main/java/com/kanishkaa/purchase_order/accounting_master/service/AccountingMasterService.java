package com.kanishkaa.purchase_order.accounting_master.service;

import com.kanishkaa.purchase_order.accounting_master.dto.api_side.AccountingMasterRequest;
import com.kanishkaa.purchase_order.accounting_master.dto.response_side.AccountingMasterResponse;
import com.kanishkaa.purchase_order.accounting_master.dto.tally_json.AccountingMasterWrapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AccountingMasterService {

    @Transactional
    AccountingMasterResponse createAccountingMaster(AccountingMasterRequest request);

    @Transactional
    void saveAllFromTally(AccountingMasterWrapper wrapper);

    AccountingMasterResponse getByIdAccountingMaster(Long id);

    List<AccountingMasterResponse> getAllAccountingMaster();

    AccountingMasterResponse updateAccountingMaster(Long id, AccountingMasterRequest request);

    @Transactional
    void  deleteByIdAccountingMaster(Long id);
}
