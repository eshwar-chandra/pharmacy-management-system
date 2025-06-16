package com.pharmacy.management.services;

import com.pharmacy.management.dto.StockDTO;
import java.util.List;

public interface StockService {
    StockDTO getStockById(Long id);
    List<StockDTO> getAllStock();
    StockDTO addStock(StockDTO stockDTO);
    StockDTO updateStock(Long id, StockDTO stockDTO);
    void deleteStock(Long id); // Soft delete
}
