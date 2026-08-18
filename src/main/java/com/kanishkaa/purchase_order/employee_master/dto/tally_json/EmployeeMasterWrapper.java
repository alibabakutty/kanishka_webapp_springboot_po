package com.kanishkaa.purchase_order.employee_master.dto.tally_json;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collections;
import java.util.List;

public record EmployeeMasterWrapper(
        @JsonProperty("Employee Master")
        List<EmployeeMasterInnerDto> employeeMasters
) {
    public List<EmployeeMasterInnerDto> getEmployeeMasters() {
        if (employeeMasters == null || employeeMasters.isEmpty()) {
            return Collections.emptyList();
        }
        return employeeMasters;
    }
}
