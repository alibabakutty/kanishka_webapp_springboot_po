package com.kanishkaa.purchase_order.employee_master.service;

import com.kanishkaa.purchase_order.employee_master.dto.api_side.EmployeeMasterRequest;
import com.kanishkaa.purchase_order.employee_master.dto.response_side.EmployeeMasterResponse;
import com.kanishkaa.purchase_order.employee_master.dto.tally_json.EmployeeMasterWrapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface EmployeeMasterService {
    @Transactional
    EmployeeMasterResponse createEmployeeMaster(EmployeeMasterRequest request);

    @Transactional
    void saveAllFromTally(EmployeeMasterWrapper wrapper);

    @Transactional
    void bulkUploadEmployeeMasters(MultipartFile file);

    EmployeeMasterResponse getByIdEmployeeMaster(Long id);

    EmployeeMasterResponse getByMobileNumber(String mobileNumber);

    List<EmployeeMasterResponse> getAllEmployeeMasters();

    EmployeeMasterResponse updateEmployeeMaster(Long id, EmployeeMasterRequest request);

    @Transactional
    EmployeeMasterResponse updateEmployeeByMobileNumber(String mobileNumber, EmployeeMasterRequest request);

    @Transactional
    void deleteByIdEmployeeMaster(Long id);
}
