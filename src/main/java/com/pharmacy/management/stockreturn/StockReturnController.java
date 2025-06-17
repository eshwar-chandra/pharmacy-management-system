package com.pharmacy.management.stockreturn; // Updated package

import com.pharmacy.management.stockreturn.StockReturnDTO; // Updated import
import com.pharmacy.management.stockreturn.StockReturnService; // Updated import
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

@Tag(name = "Stock Return Management", description = "APIs for managing stock returns")
@Slf4j
@RestController
@RequestMapping("/api/stock-returns")
@RequiredArgsConstructor
public class StockReturnController {

    private final StockReturnService stockReturnService;

    @Operation(summary = "Get a stock return by its ID", description = "Fetches a stock return based on its unique identifier.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved stock return",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StockReturnDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Stock return not found with the given ID",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<StockReturnDTO> getStockReturnById(
            @Parameter(description = "ID of the stock return to retrieve", required = true) @PathVariable Long id) {
        log.info("API request to get stock return by ID: {}", id);
        StockReturnDTO dto = stockReturnService.getStockReturnById(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Get all stock returns", description = "Retrieves a list of all stock returns.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of stock returns",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = StockReturnDTO.class)) })
    @GetMapping
    public ResponseEntity<List<StockReturnDTO>> getAllStockReturns() {
        log.info("API request to get all stock returns");
        List<StockReturnDTO> dtos = stockReturnService.getAllStockReturns();
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Create a new stock return", description = "Creates a new stock return entry. Requires valid Stock ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Stock return created successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StockReturnDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input, e.g., Stock ID not found",
                    content = @Content(schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "500", description = "An unexpected error occurred",
                    content = @Content(schema = @Schema(type = "string")))
    })
    @PostMapping
    public ResponseEntity<?> createStockReturn(
            @SwaggerRequestBody(description = "Stock return data to create. `stockId` must be valid.", required = true,
                    content = @Content(schema = @Schema(implementation = StockReturnDTO.class)))
            @Valid @org.springframework.web.bind.annotation.RequestBody StockReturnDTO stockReturnDTO) {
        log.info("API request to create stock return for stock ID: {}", stockReturnDTO.getStockId());
        // GlobalExceptionHandler will handle exceptions thrown by the service
        StockReturnDTO createdDto = stockReturnService.createStockReturn(stockReturnDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDto);
    }

    @Operation(summary = "Delete a stock return (soft delete)", description = "Marks a stock return as deleted by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Stock return deleted successfully",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Stock return not found with the given ID",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStockReturn(
            @Parameter(description = "ID of the stock return to delete", required = true) @PathVariable Long id) {
        log.info("API request to delete stock return with ID: {}", id);
        stockReturnService.deleteStockReturn(id);
        return ResponseEntity.noContent().build();
    }
}
