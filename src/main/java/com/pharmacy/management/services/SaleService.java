package com.pharmacy.management.services;
import com.pharmacy.management.dto.SaleDTO;
import java.util.List;

public interface SaleService {
    SaleDTO getSaleById(Long id);
    List<SaleDTO> getAllSales();
    SaleDTO createSale(SaleDTO saleDTO);
    // Update and Delete are complex for Sales. Start with create and read.
    // SaleDTO updateSale(Long id, SaleDTO saleDTO);
    void deleteSale(Long id); // Soft delete
}
