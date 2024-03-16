package com.pharmacy.management.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class MedicineDTO {
    private Long id;
    private String name;
    private String manufacturerName;
    private String genericName;
    private Integer sellUom;
    private BigDecimal uomPrice;
    private Integer packageSize;
    private String uniqueIdentifier;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private boolean softDelete;
}

