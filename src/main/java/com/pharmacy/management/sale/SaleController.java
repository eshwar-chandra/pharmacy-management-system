package com.pharmacy.management.sale; // Updated package

import com.pharmacy.management.sale.SaleDTO; // Updated import
import com.pharmacy.management.sale.SaleService; // Updated import
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody as SwaggerRequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Sales Management", description = "APIs for managing sales transactions")
@Slf4j
@RestController
@RequestMapping("/api/sales")
@RequiredArgsConstructor
public class SaleController {

    private final SaleService saleService;

    @Operation(summary = "Get a sale by its ID", description = "Fetches a sale transaction based on its unique identifier.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved sale",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SaleDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Sale not found with the given ID",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<SaleDTO> getSaleById(
            @Parameter(description = "ID of the sale to retrieve", required = true) @PathVariable Long id) {
        log.info("API request to get sale by ID: {}", id);
        SaleDTO saleDTO = saleService.getSaleById(id);
        return saleDTO != null ? ResponseEntity.ok(saleDTO) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Get all sales", description = "Retrieves a list of all sale transactions.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of sales",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = SaleDTO.class)) })
    @GetMapping
    public ResponseEntity<List<SaleDTO>> getAllSales() {
        log.info("API request to get all sales");
        List<SaleDTO> sales = saleService.getAllSales();
        return ResponseEntity.ok(sales);
    }

    @Operation(summary = "Create a new sale", description = "Creates a new sale transaction. Valid Medicine IDs are required for items.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Sale created successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SaleDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input, e.g., Medicine ID in an item not found",
                    content = @Content(schema = @Schema(type = "string"))), // Assuming error response is plain text
            @ApiResponse(responseCode = "500", description = "An unexpected error occurred",
                    content = @Content(schema = @Schema(type = "string")))  // Assuming error response is plain text
    })
    @PostMapping
    public ResponseEntity<?> createSale(
            @SwaggerRequestBody(description = "Sale data to create. `items.medicineId` must be valid.", required = true,
                    content = @Content(schema = @Schema(implementation = SaleDTO.class)))
            @Valid @org.springframework.web.bind.annotation.RequestBody SaleDTO saleDTO) {
        log.info("API request to create sale");
        // The GlobalExceptionHandler will catch BadRequestException and other exceptions.
        SaleDTO createdSale = saleService.createSale(saleDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSale);
    }

    @Operation(summary = "Delete a sale (soft delete)", description = "Marks a sale and its associated items as deleted by the sale's ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Sale deleted successfully",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Sale not found with the given ID",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSale(
            @Parameter(description = "ID of the sale to delete", required = true) @PathVariable Long id) {
        log.info("API request to delete sale with ID: {}", id);
        saleService.deleteSale(id);
        return ResponseEntity.noContent().build();
    }
}
