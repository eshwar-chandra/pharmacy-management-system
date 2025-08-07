package com.pharmacy.management.dtos.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalesResponseDto {
    private Long id;
    private Long userId;
    private CustomerResponseDto customer;
    private Double totalAmount;
    private List<SaleItemResponseDto> saleItems;
}