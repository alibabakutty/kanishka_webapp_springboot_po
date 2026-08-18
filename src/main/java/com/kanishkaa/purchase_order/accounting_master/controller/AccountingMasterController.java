package com.kanishkaa.purchase_order.accounting_master.controller;

import jakarta.validation.Valid;
import com.kanishkaa.purchase_order.accounting_master.dto.api_side.AccountingMasterRequest;
import com.kanishkaa.purchase_order.accounting_master.dto.response_side.AccountingMasterResponse;
import com.kanishkaa.purchase_order.accounting_master.dto.tally_json.AccountingMasterWrapper;
import com.kanishkaa.purchase_order.accounting_master.service.AccountingMasterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("api/v1/accounting-masters")
@RequiredArgsConstructor
public class AccountingMasterController {
    private final AccountingMasterService service;

//    @PostMapping("/tally")
//    public ResponseEntity<String> createTallySundryCreditor(@RequestBody String request){
//        System.out.println(request);
//        return ResponseEntity.ok("Success");
//    }

    // tally json
    @PostMapping("/tally")
    public ResponseEntity<String> createAccountingMasterFromTally(
            @RequestBody AccountingMasterWrapper wrapper
            ){
        if (wrapper == null || wrapper.getAccountingMasters().isEmpty()) {
            throw new RuntimeException("Invalid Tally JSON: Customer Master details missing");
        }

        service.saveAllFromTally(wrapper);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Successfully processed " + wrapper.getAccountingMasters().size() + " records from tally.");
    }

    @PostMapping
    public ResponseEntity<AccountingMasterResponse> create(
            @Valid @RequestBody AccountingMasterRequest request
            ){
        return new ResponseEntity<>(service.createAccountingMaster(request), HttpStatus.CREATED);
    }


    @GetMapping("/{id}")
    public ResponseEntity<AccountingMasterResponse> getById(@PathVariable Long id){
        return ResponseEntity.ok(service.getByIdAccountingMaster(id));
    }

    @GetMapping
    public ResponseEntity<List<AccountingMasterResponse>> getAll(){
        return ResponseEntity.ok(service.getAllAccountingMaster());
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountingMasterResponse> update(
            @PathVariable Long id, @Valid @RequestBody AccountingMasterRequest request
    ){
        return ResponseEntity.ok(service.updateAccountingMaster(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.deleteByIdAccountingMaster(id);
        return ResponseEntity.noContent().build();
    }
}
