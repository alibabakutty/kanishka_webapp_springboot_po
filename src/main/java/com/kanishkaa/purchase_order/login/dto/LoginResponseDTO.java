package com.kanishkaa.purchase_order.login.dto;


import com.kanishkaa.purchase_order.login.model.Role;

public record LoginResponseDTO(
        String token,
        String userEmployeeId,
        String username,
        Role role,
        String mobileNumber,
        String userImageData,
        String message
) {
}
