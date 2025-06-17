package com.pharmacy.management.sale; // Updated package

import com.pharmacy.management.sale.SalesItemDTO; // Updated import
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaleDTO {
    @Schema(description = "Sale transaction ID", example = "501", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "ID of the user who processed the sale (optional)", example = "101")
    private Long userId;

    @Schema(description = "Username of the user who processed the sale (read-only)", example = "staff_user", accessMode = Schema.AccessMode.READ_ONLY)
    private String userName;

    @Schema(description = "ID of the customer (optional, if not a walk-in)", example = "201")
    private Long customerId;

    @Schema(description = "Name of the customer (read-only)", example = "Jane Smith", accessMode = Schema.AccessMode.READ_ONLY)
    private String customerName;

    @Schema(description = "Total amount of the sale (calculated by server)", example = "11.98", accessMode = Schema.AccessMode.READ_ONLY)
    private BigDecimal totalAmount;

    @Schema(description = "Date and time of the sale", example = "2024-01-15T14:30:00Z")
    private Timestamp saleDate;

    @Schema(description = "List of items included in the sale")
    @NotEmpty(message = "Sale must have at least one item")
    @Valid
    private List<SalesItemDTO> items;

    @Schema(description = "Timestamp of sale record creation", accessMode = Schema.AccessMode.READ_ONLY)
    private Timestamp createdAt;

    @Schema(description = "Timestamp of last sale record update", accessMode = Schema.AccessMode.READ_ONLY)
    private Timestamp updatedAt;
}
