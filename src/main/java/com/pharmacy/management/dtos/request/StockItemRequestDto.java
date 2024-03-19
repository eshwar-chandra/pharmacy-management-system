package com.pharmacy.management.dtos.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockItemRequestDto {
    private Long id;
    private Long itemId;
    private Integer quantity;
    private String batchNo;
    private Double price;
    private String expiryDate;
    private String manufacturingDate;
}