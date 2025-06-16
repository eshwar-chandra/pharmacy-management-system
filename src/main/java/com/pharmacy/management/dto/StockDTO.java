package com.pharmacy.management.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

@Data
public class StockDTO {
    private Long id;
    private Long medicineId; // To link to Medicine
    private String medicineName; // For display purposes, populated in service
    private Integer quantity;
    private BigDecimal price;
    private Date expiryDate;
    private Date manufacturingDate;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
