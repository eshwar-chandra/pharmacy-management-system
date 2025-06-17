package com.pharmacy.management.stock; // Updated package

import com.pharmacy.management.stock.StockDTO; // Updated import
import com.pharmacy.management.stock.StockService; // Updated import
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

@Tag(name = "Stock Management", description = "APIs for managing medicine stock levels")
@Slf4j
@RestController
@RequestMapping("/api/stock")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @Operation(summary = "Get a stock item by its ID", description = "Fetches a stock item based on its unique identifier.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved stock item",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StockDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Stock item not found with the given ID",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<StockDTO> getStockById(
            @Parameter(description = "ID of the stock item to retrieve", required = true) @PathVariable Long id) {
        log.info("API request to get stock by ID: {}", id);
        StockDTO stockDTO = stockService.getStockById(id);
        return stockDTO != null ? ResponseEntity.ok(stockDTO) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Get all stock items", description = "Retrieves a list of all stock items.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of stock items",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = StockDTO.class)) })
    @GetMapping
    public ResponseEntity<List<StockDTO>> getAllStock() {
        log.info("API request to get all stock");
        List<StockDTO> stockItems = stockService.getAllStock();
        return ResponseEntity.ok(stockItems);
    }

    @Operation(summary = "Add a new stock item", description = "Creates a new stock item entry. Requires valid Medicine ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Stock item created successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StockDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input, e.g., Medicine ID not found",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<StockDTO> addStock(
            @SwaggerRequestBody(description = "Stock data to create. `medicineId` must be valid.", required = true,
                    content = @Content(schema = @Schema(implementation = StockDTO.class)))
            @Valid @org.springframework.web.bind.annotation.RequestBody StockDTO stockDTO) {
        log.info("API request to add stock for medicine ID: {}", stockDTO.getMedicineId());
        StockDTO createdStock = stockService.addStock(stockDTO);
        // Service layer now throws BadRequestException if medicine not found, which GlobalExceptionHandler will handle.
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStock);
    }

    @Operation(summary = "Update an existing stock item", description = "Updates details of an existing stock item by its ID. Requires valid Medicine ID if medicine is changed.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stock item updated successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StockDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Stock item not found with the given ID",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input, e.g., new Medicine ID not found",
                    content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<StockDTO> updateStock(
            @Parameter(description = "ID of the stock item to update", required = true) @PathVariable Long id,
            @SwaggerRequestBody(description = "Stock data to update. If `medicineId` is changed, it must be valid.", required = true,
                    content = @Content(schema = @Schema(implementation = StockDTO.class)))
            @Valid @org.springframework.web.bind.annotation.RequestBody StockDTO stockDTO) {
        log.info("API request to update stock with ID {}: for medicine ID {}", id, stockDTO.getMedicineId());
        StockDTO updatedStock = stockService.updateStock(id, stockDTO);
        // Service layer now throws ResourceNotFoundException or BadRequestException, handled by GlobalExceptionHandler.
        return ResponseEntity.ok(updatedStock);
    }

    @Operation(summary = "Delete a stock item (soft delete)", description = "Marks a stock item as deleted by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Stock item deleted successfully",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Stock item not found with the given ID",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStock(
            @Parameter(description = "ID of the stock item to delete", required = true) @PathVariable Long id) {
        log.info("API request to delete stock with ID: {}", id);
        stockService.deleteStock(id);
        return ResponseEntity.noContent().build();
    }
}
