package com.pharmacy.management.dtos.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleRequestDto {
    private Long id;
    private Long userId;
    private CustomerRequestDto customer;
    private Double totalAmount;
    private String saleDate;
    private List<SalesItemRequestDto> salesItems;
}