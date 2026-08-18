package com.kanishkaa.purchase_order.login.mapper;

import com.kanishkaa.purchase_order.login.dto.LoginResponseDTO;
import com.kanishkaa.purchase_order.login.dto.RegisterRequestDTO;
import com.kanishkaa.purchase_order.login.dto.UserProfileDTO;
import com.kanishkaa.purchase_order.login.model.LoginModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.Base64;

@Mapper(componentModel = "spring")
public interface LoginMapper {

    // Entity -> User Profile DTO
    @Mapping(
            target = "role",
            expression =
                    "java(user.getRole() != null ? " +
                            "user.getRole().name() : null)"
    )
    @Mapping(
            target = "mobileNumber",
            expression =
                    "java(user.getEmployee() != null ? " +
                            "user.getEmployee().getMobileNumber() : null)"
    )
    @Mapping(
            target = "userImageData",
            expression =
                    "java(toBase64(" +
                            "user.getEmployee() != null ? " +
                            "user.getEmployee().getEmployeeImageData() : null" +
                            "))"
    )
    UserProfileDTO toProfileDTO(
            LoginModel user
    );

    // Entity -> Login Response DTO
    @Mapping(
            target = "token",
            ignore = true
    )
    @Mapping(
            target = "message",
            constant = "Login successful"
    )
    @Mapping(
            target = "userEmployeeId",
            expression =
                    "java(user.getEmployee() != null ? " +
                            "user.getEmployee().getEmployeeId() : null)"
    )
    @Mapping(
            target = "mobileNumber",
            expression =
                    "java(user.getEmployee() != null ? " +
                            "user.getEmployee().getMobileNumber() : null)"
    )
    @Mapping(
            target = "userImageData",
            expression =
                    "java(toBase64(" +
                            "user.getEmployee() != null ? " +
                            "user.getEmployee().getEmployeeImageData() : null" +
                            "))"
    )
    LoginResponseDTO toLoginResponseDTO(
            LoginModel user
    );

    // DTO -> Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "lastLoginAt", ignore = true)
    @Mapping(target = "employee", ignore = true)
    @Mapping(
            target = "role",
            expression =
                    "java(dto.role() != null ? dto.role() : " +
                            "com.kanishkaa.purchase_order.login.model.Role.EMPLOYEE)"
    )
    LoginModel toEntity(
            RegisterRequestDTO dto
    );

    default String toBase64(
            byte[] imageData
    ) {
        if (imageData == null) {
            return null;
        }

        return Base64
                .getEncoder()
                .encodeToString(
                        imageData
                );
    }
}