package com.kanishkaa.purchase_order.inventory_master.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "inventory_master")
public class InventoryMasterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "inventory_master_seq")
    @SequenceGenerator(name = "inventory_master_seq", sequenceName = "inventory_master_Sequence", allocationSize = 50)
    private Long id;

    @NotBlank(message = "Item name is required")
    @Column(name = "item_name", nullable = false)
    private String itemName;

    @Column(name = "part_number", length = 20)
    private String partNumber;

    @Column(name = "item_uom", nullable = false)
    private String itemUom;

    @Column(name = "hsn_code")
    private String hsnCode;

    @Column(name = "gst_percentage", precision = 5, scale = 2)
    @DecimalMin(value = "0.0", inclusive = true)
    @DecimalMax(value = "100.0", inclusive = true)
    private BigDecimal gstPercentage;

    @Column(name = "item_rate", precision = 19, scale = 2)
    private BigDecimal itemRate;
}
