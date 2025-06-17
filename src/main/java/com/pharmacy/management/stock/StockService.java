package com.pharmacy.management.stock; // Updated package

import com.pharmacy.management.stock.StockDTO; // Updated import
import java.util.List;

public interface StockService {
    StockDTO getStockById(Long id);
    List<StockDTO> getAllStock();
    StockDTO addStock(StockDTO stockDTO);
    StockDTO updateStock(Long id, StockDTO stockDTO);
    void deleteStock(Long id); // Soft delete
}
