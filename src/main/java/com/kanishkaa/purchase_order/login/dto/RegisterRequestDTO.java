package com.kanishkaa.purchase_order.login.dto;

import com.kanishkaa.purchase_order.login.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequestDTO(
        @NotBlank String username,
        @NotBlank String password,
        @Email String email,
        Role role
) {
}
