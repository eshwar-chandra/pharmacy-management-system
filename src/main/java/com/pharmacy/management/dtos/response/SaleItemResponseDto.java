package com.pharmacy.management.dtos.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleItemResponseDto {
    private Long salesItemId;
    private Long saleId;
    private ItemResponseDto item;
    private String batchNo;
    private Integer quantity;
    private Double pricePerUnit;
}