package com.pharmacy.management.dtos.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaxResponseDto {
    private Long id;
    private String name;
    private Double rate;
    private String description;
}