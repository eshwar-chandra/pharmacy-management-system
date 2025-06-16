package com.pharmacy.management.controllers;

import com.pharmacy.management.dto.StockReturnDTO;
import com.pharmacy.management.services.StockReturnService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/stock-returns")
public class StockReturnController {

    private final StockReturnService stockReturnService;

    @Autowired
    public StockReturnController(StockReturnService stockReturnService) {
        this.stockReturnService = stockReturnService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockReturnDTO> getStockReturnById(@PathVariable Long id) {
        log.info("API request to get stock return by ID: {}", id);
        StockReturnDTO dto = stockReturnService.getStockReturnById(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<StockReturnDTO>> getAllStockReturns() {
        log.info("API request to get all stock returns");
        List<StockReturnDTO> dtos = stockReturnService.getAllStockReturns();
        return ResponseEntity.ok(dtos);
    }

    @PostMapping
    public ResponseEntity<?> createStockReturn(@RequestBody StockReturnDTO stockReturnDTO) {
        log.info("API request to create stock return for stock ID: {}", stockReturnDTO.getStockId());
        try {
            StockReturnDTO createdDto = stockReturnService.createStockReturn(stockReturnDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdDto);
        } catch (IllegalArgumentException e) {
            log.error("Error creating stock return: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error creating stock return", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStockReturn(@PathVariable Long id) {
        log.info("API request to delete stock return with ID: {}", id);
        stockReturnService.deleteStockReturn(id);
        return ResponseEntity.noContent().build();
    }
}
