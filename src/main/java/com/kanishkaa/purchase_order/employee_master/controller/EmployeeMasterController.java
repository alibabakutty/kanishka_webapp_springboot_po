package com.kanishkaa.purchase_order.employee_master.controller;

import com.kanishkaa.purchase_order.employee_master.dto.api_side.EmployeeMasterRequest;
import com.kanishkaa.purchase_order.employee_master.dto.response_side.EmployeeMasterResponse;
import com.kanishkaa.purchase_order.employee_master.service.EmployeeMasterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("api/v1/employee-masters")
@RequiredArgsConstructor
public class EmployeeMasterController {
    private final EmployeeMasterService service;

    @PostMapping("/tally")
    public ResponseEntity<String> createTallyEmployeeMaster(@RequestBody String request){
        System.out.println(request);
        return ResponseEntity.ok("Success");
    }


    // tally json
//    @PostMapping("/tally")
//    public ResponseEntity<String> createEmployeeMasterFromTally(@RequestBody EmployeeMasterWrapper wrapper){
//        if (wrapper == null || wrapper.getEmployeeMasters().isEmpty()) {
//            throw new RuntimeException("Invalid Tally JSON: Employee Master details missing");
//        }
//        service.saveAllFromTally(wrapper);
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body("Successfully processed " + wrapper.getEmployeeMasters().size() + " records from tally.");
//    }


    @PostMapping
    public ResponseEntity<EmployeeMasterResponse> create(@Valid @RequestBody EmployeeMasterRequest request){
        return new ResponseEntity<>(service.createEmployeeMaster(request), HttpStatus.CREATED);
    }

    @PostMapping(value = "/bulk-upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> bulkUploadEmployeeMaster(@RequestParam("file")MultipartFile file){
        service.bulkUploadEmployeeMasters(file);

        return ResponseEntity.ok(
                "Employee bulk upload completed successfully"
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeMasterResponse> getById(@PathVariable Long id){
        return ResponseEntity.ok(service.getByIdEmployeeMaster(id));
    }

    @GetMapping("/mobile/{mobileNumber}")
    public ResponseEntity<EmployeeMasterResponse> getByMobileNumber(@PathVariable String mobileNumber){
        return ResponseEntity.ok(service.getByMobileNumber(mobileNumber));
    }

    @GetMapping
    public ResponseEntity<List<EmployeeMasterResponse>> getAll(){
        return ResponseEntity.ok(service.getAllEmployeeMasters());
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeMasterResponse> update(@PathVariable Long id, @Valid @RequestBody EmployeeMasterRequest request){
        return ResponseEntity.ok(service.updateEmployeeMaster(id, request));
    }

    @PutMapping("/mobile/{mobileNumber}")
    public ResponseEntity<EmployeeMasterResponse> updateByMobile(@PathVariable String mobileNumber, @RequestBody EmployeeMasterRequest request){
        return ResponseEntity.ok(
                service.updateEmployeeByMobileNumber(mobileNumber, request)
        );
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.deleteByIdEmployeeMaster(id);
        return ResponseEntity.noContent().build();
    }
}
