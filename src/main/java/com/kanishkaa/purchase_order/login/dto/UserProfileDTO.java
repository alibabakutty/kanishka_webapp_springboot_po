package com.kanishkaa.purchase_order.login.dto;

import java.time.LocalDateTime;


public record UserProfileDTO(
        String username,
        String email,
        String role,
        String mobileNumber,
        String userImageData,
        LocalDateTime createdAt,
        LocalDateTime lastLoginAt
) {}
