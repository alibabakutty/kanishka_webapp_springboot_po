package com.kanishkaa.purchase_order.accounting_master.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "accounting_master")
public class AccountingMasterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "accounting_master_seq")
    @SequenceGenerator(name = "accounting_master_seq", sequenceName = "accounting_master_sequence", allocationSize = 50)
    private Long id;

    @NotBlank(message = "Sundry Creditor Name is required")
    @Column(name = "sundry_creditor_name", nullable = false)
    private String sundryCreditorName;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Email(message = "Invalid email format")
    @Column(name = "mail_id", length = 100)
    private String mailId;

    // Standard Indian PAN format: 5 letters, 4 digits, 1 letter
//    @Pattern(regexp = "^[A-Z]{5}[0-9]{4}[A-Z]{1}$", message = "Invalid PAN format")
    @Column(name = "pan_number", length = 15)
    private String panNumber;

    // Standard Indian GST format: 2 digits, 10 PAN chars, 1 digit, 1 Z, 1 digit/letter
//    @Pattern(regexp = "^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}$", message = "Invalid GST format")
    @Column(name = "gst_number", length = 20)
    private String gstNumber;

    @Column(name = "parent")
    private String parentName;

    @Column(name = "grand_parent")
    private String grandParentName;
}
