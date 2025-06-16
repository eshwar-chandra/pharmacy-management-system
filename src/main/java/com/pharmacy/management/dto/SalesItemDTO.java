package com.pharmacy.management.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class SalesItemDTO {
    private Long id; // sales_item_id
    private Long medicineId;
    private String medicineName; // For convenience
    private Integer quantity;
    private BigDecimal pricePerUnit;
    // No saleId, as it's part of SaleDTO
}
