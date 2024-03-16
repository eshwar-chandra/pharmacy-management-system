package com.pharmacy.management.models;


import java.math.BigDecimal;
import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@Table(name = "medicine")
@AllArgsConstructor
@NoArgsConstructor
public class Medicine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "manf_name", nullable = false)
    private String manufacturerName;

    @Column(name = "generic_name")
    private String genericName;

    @Column(name = "sell_uom", nullable = false)
    private Integer sellUom;

    @Column(name = "uom_price", nullable = false)
    private BigDecimal uomPrice;

    @Column(name = "package_size")
    private Integer packageSize;

    @Column(name = "unique_identifier", unique = true, nullable = false)
    private String uniqueIdentifier;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at", nullable = false)
    private Timestamp updatedAt;

    @Column(name = "soft_delete", nullable = false)
    private boolean softDelete;
}


