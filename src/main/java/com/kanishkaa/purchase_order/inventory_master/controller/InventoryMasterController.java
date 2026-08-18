package com.kanishkaa.purchase_order.inventory_master.controller;

import jakarta.validation.Valid;
import com.kanishkaa.purchase_order.inventory_master.dto.api_side.InventoryMasterRequest;
import com.kanishkaa.purchase_order.inventory_master.dto.response_side.InventoryMasterResponse;
import com.kanishkaa.purchase_order.inventory_master.dto.tally_json.InventoryMasterWrapper;
import com.kanishkaa.purchase_order.inventory_master.service.InventoryMasterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("api/v1/inventory-masters")
@RequiredArgsConstructor
public class InventoryMasterController {
    private final InventoryMasterService service;

//    @PostMapping("/tally")
//    public ResponseEntity<String> createTallyItem(@RequestBody String request){
//        System.out.println(request);
//        return ResponseEntity.ok("Success");
//    }

    // tally json
    @PostMapping("/tally")
    public ResponseEntity<String> createInventoryMasterFromTally(
            @RequestBody InventoryMasterWrapper wrapper
            ){
        if (wrapper == null || wrapper.getInventoryMasters().isEmpty()) {
            throw new RuntimeException("Invalid Tally JSON: Inventory Master details missing");
        }

        service.saveAllFromTally(wrapper);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Successfully processed " + wrapper.getInventoryMasters().size() + " records from tally.");
    }

    @PostMapping
    public ResponseEntity<InventoryMasterResponse> create(
            @Valid @RequestBody InventoryMasterRequest request
            ){
        return new ResponseEntity<>(service.createInventoryMaster(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventoryMasterResponse> getById(@PathVariable Long id){
        return ResponseEntity.ok(service.getByIdInventoryMaster(id));
    }

    @GetMapping
    public ResponseEntity<List<InventoryMasterResponse>> getAll(){
        return ResponseEntity.ok(service.getAllInventoryMasters());
    }

    @PutMapping("/{id}")
    public ResponseEntity<InventoryMasterResponse> update(
            @PathVariable Long id, @Valid @RequestBody InventoryMasterRequest request
    ){
        return ResponseEntity.ok(service.updateInventoryMaster(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.deleteByIdInventoryMaster(id);
        return ResponseEntity.noContent().build();
    }
}
