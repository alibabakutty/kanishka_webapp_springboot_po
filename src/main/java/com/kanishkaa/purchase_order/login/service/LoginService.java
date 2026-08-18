package com.kanishkaa.purchase_order.login.service;

import com.kanishkaa.purchase_order.login.dto.LoginRequestDTO;
import com.kanishkaa.purchase_order.login.dto.LoginResponseDTO;
import com.kanishkaa.purchase_order.login.dto.RegisterRequestDTO;
import com.kanishkaa.purchase_order.login.dto.UserProfileDTO;
import org.springframework.http.ResponseEntity;
import java.util.Map;

public interface LoginService {


    // LOGIN
    ResponseEntity<LoginResponseDTO> login(
            LoginRequestDTO loginRequestDTO
    );

    // REGISTER
    ResponseEntity<Map<String, String>> register(
            RegisterRequestDTO registerRequestDTO
    );

    // PROFILE
    ResponseEntity<UserProfileDTO> getCurrentUser(
            String username
    );

    // LOGOUT
    ResponseEntity<Map<String, String>> logout();
}
