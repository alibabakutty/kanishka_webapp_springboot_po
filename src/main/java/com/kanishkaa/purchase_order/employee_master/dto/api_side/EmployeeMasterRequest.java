package com.kanishkaa.purchase_order.employee_master.dto.api_side;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record EmployeeMasterRequest(
        @NotBlank String employeeId,
        @NotBlank String employeeName,
        @NotBlank String mobileNumber,
        LocalDate dateOfJoining,
        String aadhaarNumber,
        String panNumber,
        @NotBlank String email,
        @NotBlank String password,
        byte[] employeeImageData
) {
}
