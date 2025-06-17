package com.pharmacy.management.stockreturn; // Updated package

import com.pharmacy.management.common.exception.BadRequestException; // Updated import
import com.pharmacy.management.common.exception.ResourceNotFoundException; // Updated import
import com.pharmacy.management.stock.Stock; // Updated import
import com.pharmacy.management.stock.StockRepository; // Updated import
import com.pharmacy.management.stockreturn.StockReturnDTO; // Updated import
import com.pharmacy.management.stockreturn.StockReturnMapper; // Updated import
import com.pharmacy.management.stockreturn.StockReturn; // Updated import
import com.pharmacy.management.stockreturn.StockReturnRepository; // Updated import
import com.pharmacy.management.stockreturn.StockReturnService; // Updated import

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
// import java.util.Optional; // No longer needed
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockReturnServiceImpl implements StockReturnService {

    private final StockReturnRepository stockReturnRepository;
    private final StockReturnMapper stockReturnMapper;
    private final StockRepository stockRepository;

    @Override
    public StockReturnDTO getStockReturnById(Long id) {
        log.info("Fetching stock return with ID: {}", id);
        return stockReturnRepository.findById(id)
                .map(stockReturnMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("StockReturn not found with ID: " + id));
    }

    @Override
    public List<StockReturnDTO> getAllStockReturns() {
        log.info("Fetching all stock returns");
        return stockReturnRepository.findAll().stream()
                .map(stockReturnMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public StockReturnDTO createStockReturn(StockReturnDTO stockReturnDTO) {
        log.info("Creating new stock return for stock ID: {}", stockReturnDTO.getStockId());
        StockReturn stockReturn = stockReturnMapper.toEntity(stockReturnDTO);

        Stock stock = stockRepository.findById(stockReturnDTO.getStockId())
                .orElseThrow(() -> new BadRequestException("Stock not found with ID: " + stockReturnDTO.getStockId() + ". Cannot create stock return."));

        stockReturn.setStock(stock);
        StockReturn savedStockReturn = stockReturnRepository.save(stockReturn);
        log.info("Stock return created with ID: {}", savedStockReturn.getReturnId());
        return stockReturnMapper.toDTO(savedStockReturn);
    }

    @Override
    @Transactional
    public void deleteStockReturn(Long id) {
        log.info("Soft deleting stock return with ID: {}", id);
        StockReturn stockReturn = stockReturnRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("StockReturn not found with ID: " + id));
        stockReturn.setSoftDelete(true);
        stockReturnRepository.save(stockReturn);
    }
}
