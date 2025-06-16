package com.pharmacy.management.services.impl;

import com.pharmacy.management.dto.StockReturnDTO;
import com.pharmacy.management.mapper.StockReturnMapper;
import com.pharmacy.management.models.Stock;
import com.pharmacy.management.models.StockReturn;
import com.pharmacy.management.repositories.StockRepository;
import com.pharmacy.management.repositories.StockReturnRepository;
import com.pharmacy.management.services.StockReturnService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class StockReturnServiceImpl implements StockReturnService {

    private final StockReturnRepository stockReturnRepository;
    private final StockReturnMapper stockReturnMapper;
    private final StockRepository stockRepository; // To validate Stock ID

    @Autowired
    public StockReturnServiceImpl(StockReturnRepository stockReturnRepository,
                                  StockReturnMapper stockReturnMapper,
                                  StockRepository stockRepository) {
        this.stockReturnRepository = stockReturnRepository;
        this.stockReturnMapper = stockReturnMapper;
        this.stockRepository = stockRepository;
    }

    @Override
    public StockReturnDTO getStockReturnById(Long id) {
        log.info("Fetching stock return with ID: {}", id);
        return stockReturnRepository.findById(id)
                .map(stockReturnMapper::toDTO)
                .orElse(null);
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

        Optional<Stock> stockOpt = stockRepository.findById(stockReturnDTO.getStockId());
        if (!stockOpt.isPresent()) {
            log.error("Stock with ID {} not found. Cannot create stock return.", stockReturnDTO.getStockId());
            throw new IllegalArgumentException("Stock not found: " + stockReturnDTO.getStockId());
        }
        // Potentially validate if quantityReturned <= stock.getQuantity()
        // And then update stock.setQuantity(stock.getQuantity() - stockReturn.getQuantityReturned());
        // stockRepository.save(stockOpt.get()); // If updating stock quantity
        // For now, just record the return.

        stockReturn.setStock(stockOpt.get());
        StockReturn savedStockReturn = stockReturnRepository.save(stockReturn);
        log.info("Stock return created with ID: {}", savedStockReturn.getReturnId());
        return stockReturnMapper.toDTO(savedStockReturn);
    }

    // updateStockReturn would be here if implemented

    @Override
    @Transactional
    public void deleteStockReturn(Long id) {
        log.info("Soft deleting stock return with ID: {}", id);
        Optional<StockReturn> optionalStockReturn = stockReturnRepository.findById(id);
        if (optionalStockReturn.isPresent()) {
            StockReturn stockReturn = optionalStockReturn.get();
            stockReturn.setSoftDelete(true);
            stockReturnRepository.save(stockReturn);
        } else {
            log.warn("Stock return with ID {} not found for deletion", id);
        }
    }
}
