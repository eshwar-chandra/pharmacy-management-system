package com.pharmacy.management.medicine; // Updated package

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicineDTO {
    @Schema(description = "Unique identifier of the medicine", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Name of the medicine", example = "Paracetamol 500mg")
    @NotBlank(message = "Medicine name cannot be blank")
    private String name;

    @Schema(description = "Name of the manufacturer", example = "Pharma Inc.")
    @NotBlank(message = "Manufacturer name cannot be blank")
    private String manufacturerName;

    @Schema(description = "Generic name of the medicine", example = "Acetaminophen")
    private String genericName; // Optional

    @Schema(description = "Selling unit of measure (e.g., count of tablets, ml in bottle)", example = "10")
    @NotNull(message = "Sell UOM cannot be null")
    @Min(value = 1, message = "Sell UOM must be at least 1")
    private Integer sellUom;

    @Schema(description = "Price per unit of measure", example = "5.99")
    @NotNull(message = "UOM price cannot be null")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    private BigDecimal uomPrice;

    @Schema(description = "Number of units in a package", example = "20")
    @Min(value = 0, message = "Package size cannot be negative")
    private Integer packageSize; // Optional, but if present, must be valid

    @Schema(description = "Unique identifier code for the medicine (e.g., NDC, barcode)", example = "NDC12345-678-01")
    @NotBlank(message = "Unique identifier cannot be blank")
    private String uniqueIdentifier;

    @Schema(description = "Timestamp of creation", accessMode = Schema.AccessMode.READ_ONLY)
    private Timestamp createdAt;

    @Schema(description = "Timestamp of last update", accessMode = Schema.AccessMode.READ_ONLY)
    private Timestamp updatedAt;

    @Schema(description = "Soft delete status", accessMode = Schema.AccessMode.READ_ONLY)
    private boolean softDelete;
}
