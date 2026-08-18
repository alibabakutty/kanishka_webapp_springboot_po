package com.kanishkaa.purchase_order.purchase_order.controller;

import com.kanishkaa.purchase_order.purchase_order.converter.PurchaseOrderConverter;
import com.kanishkaa.purchase_order.purchase_order.dto.api_side.PurchaseOrderRequest;
import com.kanishkaa.purchase_order.purchase_order.dto.response_side.MonthlySummaryLabourResponse;
import com.kanishkaa.purchase_order.purchase_order.dto.response_side.MonthlySummaryMaterialResponse;
import com.kanishkaa.purchase_order.purchase_order.dto.response_side.PurchaseOrderResponse;
import com.kanishkaa.purchase_order.purchase_order.dto.tally_json.PurchaseOrderWrapper;
import com.kanishkaa.purchase_order.purchase_order.service.PurchaseOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/purchase-orders")
@RequiredArgsConstructor
public class PurchaseOrderController {

    private final PurchaseOrderService service;
    private final PurchaseOrderConverter converter;

//    @PostMapping
//    public ResponseEntity<PurchaseOrderResponse> create(@RequestBody PurchaseOrderRequest request) {
//        PurchaseOrderResponse response = service.create(request);
//        return ResponseEntity.status(201).body(response);
//    }

//    @PostMapping("/tally")
//    public ResponseEntity<String> create(@RequestBody String request) {
//        System.out.println(request);
//        return ResponseEntity.ok("Success");
//    }


    // tally json
    @PostMapping("/tally")
    public ResponseEntity<PurchaseOrderResponse> createPurchaseOrderFromTally(
            @RequestBody PurchaseOrderWrapper wrapper
    ) {

        System.out.println("🔥 CONTROLLER HIT");
        System.out.println("Request = " + wrapper);

        try {

            if (wrapper == null || wrapper.voucherDetails() == null) {
                throw new RuntimeException("Invalid Tally JSON: Voucher Details missing");
            }

            System.out.println("🔥 BEFORE CONVERTER");

            PurchaseOrderRequest request = converter.fromTallyJson(wrapper);

            System.out.println("🔥 AFTER CONVERTER");
            System.out.println(request);

            System.out.println("🔥 BEFORE SERVICE");

            PurchaseOrderResponse response = service.create(request);

            System.out.println("🔥 AFTER SERVICE");

            return ResponseEntity.status(201).body(response);

        } catch (Exception e) {
            System.out.println("🔥 ERROR OCCURRED");
            e.printStackTrace();
            throw e;
        }
    }

    // Normal JSON (API Format)
    @PostMapping
    public ResponseEntity<PurchaseOrderResponse> createPurchaseOrder(@RequestBody PurchaseOrderRequest request) {
        System.out.println("NORMAL API HIT ✅" + request);
        return ResponseEntity.status(201).body(service.create(request));
    }

//    @PostMapping
//    public PurchaseOrderResponse createTally(@RequestBody String rawJson) {
//        System.out.println(rawJson);
//        return null;
//    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseOrderResponse> getById(@PathVariable Long id){
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/allpo")
    public ResponseEntity<List<PurchaseOrderResponse>> getAllPurchaseOrders() {
        return ResponseEntity.ok(service.getpo());
    }

    @GetMapping("/generalpo")
    public ResponseEntity<List<PurchaseOrderResponse>> getAll() {
        return ResponseEntity.ok(service.generalpo());
    }

    @GetMapping("/materialpo")
    public ResponseEntity<List<PurchaseOrderResponse>> materialpo() {
        return ResponseEntity.ok(service.materialpo());
    }

    @GetMapping("/labourpo")
    public ResponseEntity<List<PurchaseOrderResponse>> labourpo() {
        return ResponseEntity.ok(service.labourpo());
    }

//    @GetMapping("/materialpo/monthly-summary")
//    public ResponseEntity<java.util.Map<String, Long>> getMonthlySummary(@RequestParam int financialYearStart){
//        java.util.Map<String, Long> summary = new java.util.LinkedHashMap<>();
//        String[] monthNames = {"April", "May", "June", "July", "August", "September", "October", "November", "December", "January", "February", "March"};
//        int[] months = {4, 5, 6, 7, 8, 9, 10, 11, 12, 1, 2, 3};
//
//        for(int i = 0; i < months.length; i++){
//            int targetYear = (months[i] >=4 ? financialYearStart : financialYearStart + 1);
//            long count = service.getMaterialPOsByMonth(targetYear, months[i]).size();
//            summary.put(monthNames[i], count);
//        }
//    return ResponseEntity.ok(summary);
//    }

    @GetMapping("/materialpo/monthly-summary")
    public ResponseEntity<java.util.Map<String, MonthlySummaryMaterialResponse>> getMonthlyMaterialSummary(
            @RequestParam int financialYearStart) {

        java.util.Map<String, MonthlySummaryMaterialResponse> summary = new java.util.LinkedHashMap<>();
        String[] monthNames = {"April", "May", "June", "July", "August", "September", "October", "November", "December", "January", "February", "March"};
        int[] months = {4, 5, 6, 7, 8, 9, 10, 11, 12, 1, 2, 3};

        for (int i = 0; i < months.length; i++) {
            int targetYear = (months[i] >= 4 ? financialYearStart : financialYearStart + 1);

            // This query runs lighting fast because it only returns 1 row with counts & totals
            MonthlySummaryMaterialResponse monthSummary = service.getMaterialSummaryByMonth(targetYear, months[i]);
            summary.put(monthNames[i], monthSummary);
        }
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/materialpo/details")
    public ResponseEntity<List<PurchaseOrderResponse>> getMonthlyMaterialDetails(@RequestParam int year, @RequestParam int month){
        return ResponseEntity.ok(service.getMaterialPOsByMonth(year, month));
    }


    @GetMapping("/labourpo/monthly-summary")
    public ResponseEntity<java.util.Map<String, MonthlySummaryLabourResponse>> getMonthlyLabourSummary(
            @RequestParam int financialYearStart
    ){
        java.util.Map<String, MonthlySummaryLabourResponse> summary = new java.util.LinkedHashMap<>();
        String[] monthNames = {"April", "May", "June", "July", "August", "September", "October", "November", "December", "January", "February", "March"};
        int[] months = {4, 5, 6, 7, 8, 9, 10, 11, 12, 1, 2, 3};

        for (int i = 0; i < months.length; i++) {
            int targetYear = (months[i] >= 4 ? financialYearStart : financialYearStart + 1);

            MonthlySummaryLabourResponse monthSummary = service.getLabourSummaryByMonth(targetYear, months[i]);
            summary.put(monthNames[i], monthSummary);
        }
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/labourpo/details")
    public ResponseEntity<List<PurchaseOrderResponse>> getMonthlyLabourDetails(@RequestParam int year, @RequestParam int month){
        return ResponseEntity.ok(service.getLabourPOsByMonth(year, month));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PurchaseOrderResponse> update(@PathVariable Long id, @RequestBody PurchaseOrderRequest request){
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
