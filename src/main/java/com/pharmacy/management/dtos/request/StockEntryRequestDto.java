package com.pharmacy.management.dtos.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockEntryRequestDto {
    private Long id;
    private Long supplierId;
    private String entryDate;
    private String grn;
    private String phoneNumber;
    private String address;
    private List<StockEntryRequestDto> stockItems;
}