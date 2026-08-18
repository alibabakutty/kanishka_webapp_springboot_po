package com.kanishkaa.purchase_order.login.service.serviceImpl;

import com.kanishkaa.purchase_order.exception.duplicateUserException.DuplicateUserException;
import com.kanishkaa.purchase_order.exception.invalidCredentailsException.InvalidCredentialsException;
import com.kanishkaa.purchase_order.exception.userNotFoundException.UserNotFoundException;
import com.kanishkaa.purchase_order.login.dto.LoginRequestDTO;
import com.kanishkaa.purchase_order.login.dto.LoginResponseDTO;
import com.kanishkaa.purchase_order.login.dto.RegisterRequestDTO;
import com.kanishkaa.purchase_order.login.dto.UserProfileDTO;
import com.kanishkaa.purchase_order.login.mapper.LoginMapper;
import com.kanishkaa.purchase_order.login.model.LoginModel;
import com.kanishkaa.purchase_order.login.repository.LoginRepository;
import com.kanishkaa.purchase_order.login.service.LoginService;
import com.kanishkaa.purchase_order.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class LoginServiceImpl implements LoginService {

    private final JwtUtils jwtUtils;
    private final LoginRepository loginRepository;
    private final PasswordEncoder passwordEncoder;
    private final LoginMapper loginMapper;

    // LOGIN
    @Override
    public ResponseEntity<LoginResponseDTO> login(
            LoginRequestDTO loginRequestDTO
    ) {
        validateLoginInput(
                loginRequestDTO
        );

        LoginModel user = loginRepository
                .findByUsername(
                        loginRequestDTO.username()
                )
                .orElseThrow(
                        () -> new InvalidCredentialsException(
                                "Invalid username"
                        )
                );

        if (!passwordEncoder.matches(
                loginRequestDTO.password(),
                user.getPassword()
        )) {
            throw new InvalidCredentialsException(
                    "Invalid password"
            );
        }

        user.setLastLoginAt(
                LocalDateTime.now()
        );

        loginRepository.save(
                user
        );

        ResponseCookie jwtCookie =
                jwtUtils.generateJwtCookie(
                        user.getUsername()
                );

        LoginResponseDTO mapped =
                loginMapper.toLoginResponseDTO(
                        user
                );

        LoginResponseDTO response =
                new LoginResponseDTO(
                        jwtCookie.getValue(),
                        mapped.userEmployeeId(),
                        mapped.username(),
                        mapped.role(),
                        mapped.mobileNumber(),
                        mapped.userImageData(),
                        mapped.message()
                );

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.SET_COOKIE,
                        jwtCookie.toString()
                )
                .body(
                        response
                );
    }

    // REGISTER
    @Override
    public ResponseEntity<Map<String, String>> register(
            RegisterRequestDTO registerRequestDTO
    ) {
        validateRegistrationInput(
                registerRequestDTO
        );

        if (loginRepository.existsByUsername(
                registerRequestDTO.username()
        )) {
            throw new DuplicateUserException(
                    "Username '" +
                            registerRequestDTO.username() +
                            "' is already taken"
            );
        }

        if (loginRepository.existsByEmail(
                registerRequestDTO.email()
        )) {
            throw new DuplicateUserException(
                    "Email already registered"
            );
        }

        LoginModel user =
                loginMapper.toEntity(
                        registerRequestDTO
                );

        user.setPassword(
                passwordEncoder.encode(
                        registerRequestDTO.password()
                )
        );

        loginRepository.save(
                user
        );

        return ResponseEntity
                .status(
                        HttpStatus.CREATED
                )
                .body(
                        Map.of(
                                "message",
                                "User registered successfully"
                        )
                );
    }

    // PROFILE
    @Override
    public ResponseEntity<UserProfileDTO> getCurrentUser(
            String username
    ) {
        LoginModel user = loginRepository
                .findByUsername(
                        username
                )
                .orElseThrow(
                        () -> new UserNotFoundException(
                                "User not found: " + username
                        )
                );

        return ResponseEntity.ok(
                loginMapper.toProfileDTO(
                        user
                )
        );
    }

    // LOGOUT
    @Override
    public ResponseEntity<Map<String, String>> logout() {
        ResponseCookie cleanCookie =
                jwtUtils.getCleanJwtCookie();

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.SET_COOKIE,
                        cleanCookie.toString()
                )
                .body(
                        Map.of(
                                "message",
                                "Logout Successful"
                        )
                );
    }

    private void validateLoginInput(
            LoginRequestDTO dto
    ) {
        if (dto == null) {
            throw new IllegalArgumentException(
                    "Data cannot be null"
            );
        }
    }

    private void validateRegistrationInput(
            RegisterRequestDTO dto
    ) {
        if (dto == null) {
            throw new IllegalArgumentException(
                    "Data cannot be null"
            );
        }
    }
}