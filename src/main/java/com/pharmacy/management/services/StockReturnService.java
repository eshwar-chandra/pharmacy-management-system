package com.pharmacy.management.services;

import com.pharmacy.management.dto.StockReturnDTO;
import java.util.List;

public interface StockReturnService {
    StockReturnDTO getStockReturnById(Long id);
    List<StockReturnDTO> getAllStockReturns();
    StockReturnDTO createStockReturn(StockReturnDTO stockReturnDTO);
    // Update might be limited (e.g. only reason)
    // StockReturnDTO updateStockReturn(Long id, StockReturnDTO stockReturnDTO);
    void deleteStockReturn(Long id); // Soft delete
}
