package com.pharmacy.management.controllers;

import com.pharmacy.management.dto.StockDTO;
import com.pharmacy.management.services.StockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/stock")
public class StockController {

    private final StockService stockService;

    @Autowired
    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockDTO> getStockById(@PathVariable Long id) {
        log.info("API request to get stock by ID: {}", id);
        StockDTO stockDTO = stockService.getStockById(id);
        return stockDTO != null ? ResponseEntity.ok(stockDTO) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<StockDTO>> getAllStock() {
        log.info("API request to get all stock");
        List<StockDTO> stockItems = stockService.getAllStock();
        return ResponseEntity.ok(stockItems);
    }

    @PostMapping
    public ResponseEntity<StockDTO> addStock(@RequestBody StockDTO stockDTO) {
        log.info("API request to add stock for medicine ID: {}", stockDTO.getMedicineId());
        StockDTO createdStock = stockService.addStock(stockDTO);
        if (createdStock == null) {
            // Likely due to medicine not found
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStock);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StockDTO> updateStock(@PathVariable Long id, @RequestBody StockDTO stockDTO) {
        log.info("API request to update stock with ID {}: for medicine ID {}", id, stockDTO.getMedicineId());
        StockDTO updatedStock = stockService.updateStock(id, stockDTO);
         if (updatedStock == null) {
            // Likely due to stock or medicine not found
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(updatedStock);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStock(@PathVariable Long id) {
        log.info("API request to delete stock with ID: {}", id);
        stockService.deleteStock(id);
        return ResponseEntity.noContent().build();
    }
}
