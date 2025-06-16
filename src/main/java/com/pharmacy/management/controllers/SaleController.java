package com.pharmacy.management.controllers;

import com.pharmacy.management.dto.SaleDTO;
import com.pharmacy.management.services.SaleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/sales")
public class SaleController {

    private final SaleService saleService;

    @Autowired
    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaleDTO> getSaleById(@PathVariable Long id) {
        log.info("API request to get sale by ID: {}", id);
        SaleDTO saleDTO = saleService.getSaleById(id);
        return saleDTO != null ? ResponseEntity.ok(saleDTO) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<SaleDTO>> getAllSales() {
        log.info("API request to get all sales");
        List<SaleDTO> sales = saleService.getAllSales();
        return ResponseEntity.ok(sales);
    }

    @PostMapping
    public ResponseEntity<?> createSale(@RequestBody SaleDTO saleDTO) {
        log.info("API request to create sale");
        try {
            SaleDTO createdSale = saleService.createSale(saleDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdSale);
        } catch (IllegalArgumentException e) {
            log.error("Error creating sale: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error creating sale", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSale(@PathVariable Long id) {
        log.info("API request to delete sale with ID: {}", id);
        saleService.deleteSale(id);
        return ResponseEntity.noContent().build();
    }
}
