package com.kanishkaa.purchase_order.employee_master.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "employee_master")
public class EmployeeMasterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employee_master_seq")
    @SequenceGenerator(name = "employee_master_seq", sequenceName = "employee_master_sequence", allocationSize = 50)
    private Long id;

    @NotBlank(message = "Employee ID is required")
    @Column(name = "employee_id", unique = true, nullable = false)
    private String employeeId;

    @NotBlank(message = "Employee name is required")
    @Column(name = "employee_name", nullable = false)
    private String employeeName;

    @NotBlank(message = "Mobile number is required")
    @Size(min = 10, max = 15, message = "Mobile number must be between 10 to 15")
    @Column(name = "mobile_number")
    private String mobileNumber;

    @Column(name = "date_of_joining")
    private LocalDate dateOfJoining;

    @Pattern(regexp = "^[0-9]{12}$", message = "Aadhaar must be 12 digits")
    @Column(name = "aadhaar_number", unique = true)
    private String aadhaarNumber;

    //    @Pattern(regexp = "[A-Z]{5}[0-9]{4}[A-Z]{1}", message = "Invalid PAN format")
    @Column(name = "pan_number", unique = true)
    private String panNumber;

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Column(name = "password", nullable = false)
    @ToString.Exclude
    private String password;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "employee_image_data")
    @ToString.Exclude
    private byte[] employeeImageData;

    @PrePersist
    protected void onCreate(){
        this.createdAt = LocalDateTime.now();
    }
}
