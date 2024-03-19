package com.pharmacy.management.dtos.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalesItemRequestDto {
    private Long salesItemId;
    private Long itemId;
    private String batchNo;
    private Integer quantity;
    private Double pricePerUnit;
}