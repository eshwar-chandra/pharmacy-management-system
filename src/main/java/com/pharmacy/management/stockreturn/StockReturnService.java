package com.pharmacy.management.stockreturn; // Updated package

import com.pharmacy.management.stockreturn.StockReturnDTO; // Updated import
import java.util.List;

public interface StockReturnService {
    StockReturnDTO getStockReturnById(Long id);
    List<StockReturnDTO> getAllStockReturns();
    StockReturnDTO createStockReturn(StockReturnDTO stockReturnDTO);
    void deleteStockReturn(Long id); // Soft delete
}
