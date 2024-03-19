package com.pharmacy.management.dtos.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplierRequestDto {
    private Long supplierId;
    private String name;
    private String phoneNumber;
    private String email;
    private String address;
}