package com.pharmacy.management.sale; // Updated package

import com.pharmacy.management.sale.SaleDTO; // Updated import
import java.util.List;

public interface SaleService {
    SaleDTO getSaleById(Long id);
    List<SaleDTO> getAllSales();
    SaleDTO createSale(SaleDTO saleDTO);
    void deleteSale(Long id); // Soft delete
}
