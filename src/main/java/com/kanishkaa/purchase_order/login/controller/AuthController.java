package com.kanishkaa.purchase_order.login.controller;

import com.kanishkaa.purchase_order.login.dto.LoginRequestDTO;
import com.kanishkaa.purchase_order.login.dto.LoginResponseDTO;
import com.kanishkaa.purchase_order.login.dto.RegisterRequestDTO;
import com.kanishkaa.purchase_order.login.dto.UserProfileDTO;
import com.kanishkaa.purchase_order.login.service.LoginService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final LoginService loginService;

    // =========================
    // LOGIN
    // =========================
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> loginController(
            @Valid @RequestBody LoginRequestDTO loginRequestDTO
    ) {
        return loginService.login(
                loginRequestDTO
        );
    }

    // =========================
    // REGISTER
    // =========================
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerController(
            @Valid @RequestBody RegisterRequestDTO registerRequestDTO
    ) {
        return loginService.register(
                registerRequestDTO
        );
    }

    // =========================
    // LOGOUT
    // =========================
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logoutController() {
        return loginService.logout();
    }

    // =========================
    // CURRENT USER PROFILE
    // =========================
    @GetMapping("/me")
    public ResponseEntity<UserProfileDTO> getMyProfile(
            Principal principal
    ) {
        if (principal == null) {
            return ResponseEntity
                    .status(
                            HttpStatus.UNAUTHORIZED
                    )
                    .build();
        }

        return loginService.getCurrentUser(
                principal.getName()
        );
    }
}