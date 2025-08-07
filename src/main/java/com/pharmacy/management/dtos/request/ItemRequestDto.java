package com.pharmacy.management.dtos.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestDto {
    private Long id;
    private String name;
    private String manfName;
    private String genericName;
    private Integer sellUom;
    private Double uomPrice;
    private Integer packageSize;
    private String uniqueIdentifier;
    private String generalText;
    private List<TaxRequestDto> taxes;
}