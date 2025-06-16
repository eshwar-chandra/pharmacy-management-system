package com.pharmacy.management.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Data
public class SaleDTO {
    private Long id; // sale_id
    private Long userId;
    private String userName; // For display
    private Long customerId;
    private String customerName; // For display
    private BigDecimal totalAmount;
    private Timestamp saleDate;
    private List<SalesItemDTO> items;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
