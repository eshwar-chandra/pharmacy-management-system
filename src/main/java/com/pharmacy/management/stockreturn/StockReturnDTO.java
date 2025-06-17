package com.pharmacy.management.stockreturn; // Updated package

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockReturnDTO {
    @Schema(description = "Stock return ID", example = "601", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "ID of the stock item being returned", example = "301")
    @NotNull(message = "Stock ID cannot be null for a return")
    private Long stockId;

    @Schema(description = "Name of the medicine being returned (read-only)", example = "Aspirin 100mg", accessMode = Schema.AccessMode.READ_ONLY)
    private String medicineName;

    @Schema(description = "Quantity of the stock item being returned", example = "10")
    @NotNull(message = "Quantity returned cannot be null")
    @Min(value = 1, message = "Quantity returned must be at least 1")
    private Integer quantityReturned;

    @Schema(description = "Reason for the stock return (optional)", example = "Damaged goods")
    @Size(max = 255, message = "Reason cannot be longer than 255 characters")
    private String reason;

    @Schema(description = "Date and time of the stock return", example = "2024-01-20T10:00:00Z")
    private Timestamp returnDate;

    @Schema(description = "Timestamp of stock return record creation", accessMode = Schema.AccessMode.READ_ONLY)
    private Timestamp createdAt;

    @Schema(description = "Timestamp of last stock return record update", accessMode = Schema.AccessMode.READ_ONLY)
    private Timestamp updatedAt;
}
