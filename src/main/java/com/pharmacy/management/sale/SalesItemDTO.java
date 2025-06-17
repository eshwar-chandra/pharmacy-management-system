package com.pharmacy.management.sale; // Updated package

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesItemDTO {
    @Schema(description = "Sales item ID (usually read-only as it's generated)", example = "401", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "ID of the medicine sold", example = "1")
    @NotNull(message = "Medicine ID in item cannot be null")
    private Long medicineId;

    @Schema(description = "Name of the medicine sold (read-only, derived from medicineId)", example = "Paracetamol 500mg", accessMode = Schema.AccessMode.READ_ONLY)
    private String medicineName;

    @Schema(description = "Quantity of the medicine sold", example = "2")
    @NotNull(message = "Quantity in item cannot be null")
    @Min(value = 1, message = "Quantity in item must be at least 1")
    private Integer quantity;

    @Schema(description = "Price per unit at the time of sale", example = "5.99")
    @NotNull(message = "Price per unit in item cannot be null")
    @DecimalMin(value = "0.00", message = "Price per unit in item cannot be negative")
    private BigDecimal pricePerUnit;
}
