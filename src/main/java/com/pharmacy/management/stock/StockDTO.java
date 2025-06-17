package com.pharmacy.management.stock; // Updated package

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockDTO {
    @Schema(description = "Stock item ID", example = "301", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "ID of the medicine in this stock item", example = "1")
    @NotNull(message = "Medicine ID cannot be null")
    private Long medicineId;

    @Schema(description = "Name of the medicine (read-only, derived from medicineId)", example = "Paracetamol 500mg", accessMode = Schema.AccessMode.READ_ONLY)
    private String medicineName;

    @Schema(description = "Quantity of the medicine in stock", example = "100")
    @NotNull(message = "Quantity cannot be null")
    @Min(value = 0, message = "Quantity cannot be negative")
    private Integer quantity;

    @Schema(description = "Purchase price of this stock item", example = "4.50")
    @NotNull(message = "Price cannot be null")
    @DecimalMin(value = "0.00", message = "Price cannot be negative")
    private BigDecimal price;

    @Schema(description = "Expiry date of this stock item", example = "2025-12-31")
    @FutureOrPresent(message = "Expiry date must be in the present or future")
    private Date expiryDate; // Optional

    @Schema(description = "Manufacturing date of this stock item", example = "2023-01-01")
    private Date manufacturingDate; // Optional

    @Schema(description = "Timestamp of stock item creation", accessMode = Schema.AccessMode.READ_ONLY)
    private Timestamp createdAt;

    @Schema(description = "Timestamp of last stock item update", accessMode = Schema.AccessMode.READ_ONLY)
    private Timestamp updatedAt;
}
