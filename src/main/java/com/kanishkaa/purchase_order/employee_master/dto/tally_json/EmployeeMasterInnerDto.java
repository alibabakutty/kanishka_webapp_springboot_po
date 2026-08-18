package com.kanishkaa.purchase_order.employee_master.dto.tally_json;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record EmployeeMasterInnerDto(

        @JsonProperty("employeeId")
        String employeeId,
        @JsonProperty("employeeName")
        String employeeName,
        @JsonProperty("mobileNumber")
        String mobileNumber,
        @JsonProperty("dateOfJoining")
        LocalDate dateOfJoining,
        @JsonProperty("aadhaarNumber")
        String aadhaarNumber,
        @JsonProperty("panNumber")
        String panNumber,
        @JsonProperty("email")
        String email,
        @JsonProperty("password")
        String password,
        @JsonProperty("createdAt")
        LocalDateTime createdAt,
        @JsonProperty("employeeImageData")
        byte[] employeeImageData
) {
}
