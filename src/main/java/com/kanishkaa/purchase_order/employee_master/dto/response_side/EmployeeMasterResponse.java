package com.kanishkaa.purchase_order.employee_master.dto.response_side;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record EmployeeMasterResponse(
        Long id,
        String employeeId,
        String employeeName,
        String mobileNumber,
        LocalDate dateOfJoining,
        String aadhaarNumber,
        String panNumber,
        String email,
        String password,
        LocalDateTime createdAt,
        byte[] employeeImageData
) {
}
