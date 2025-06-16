package com.pharmacy.management.services.impl;

import com.pharmacy.management.dto.StockDTO;
import com.pharmacy.management.mapper.StockMapper;
import com.pharmacy.management.models.Medicine;
import com.pharmacy.management.models.Stock;
import com.pharmacy.management.repositories.MedicineRepository;
import com.pharmacy.management.repositories.StockRepository;
import com.pharmacy.management.services.StockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;
    private final StockMapper stockMapper;
    private final MedicineRepository medicineRepository; // To fetch Medicine entities

    @Autowired
    public StockServiceImpl(StockRepository stockRepository, StockMapper stockMapper, MedicineRepository medicineRepository) {
        this.stockRepository = stockRepository;
        this.stockMapper = stockMapper;
        this.medicineRepository = medicineRepository;
    }

    @Override
    public StockDTO getStockById(Long id) {
        log.info("Fetching stock with ID: {}", id);
        Optional<Stock> optionalStock = stockRepository.findById(id);
        return optionalStock.map(stockMapper::toDTO).orElse(null);
    }

    @Override
    public List<StockDTO> getAllStock() {
        log.info("Fetching all stock");
        return stockRepository.findAll().stream()
                .map(stockMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public StockDTO addStock(StockDTO stockDTO) {
        log.info("Adding new stock for medicine ID: {}", stockDTO.getMedicineId());
        Stock stock = stockMapper.toEntity(stockDTO);
        Optional<Medicine> medicine = medicineRepository.findById(stockDTO.getMedicineId());
        if (!medicine.isPresent()) {
            log.error("Medicine with ID {} not found. Cannot add stock.", stockDTO.getMedicineId());
            // Or throw a custom exception
            return null;
        }
        stock.setMedicine(medicine.get());
        return stockMapper.toDTO(stockRepository.save(stock));
    }

    @Override
    @Transactional
    public StockDTO updateStock(Long id, StockDTO stockDTO) {
        log.info("Updating stock with ID {}: for medicine ID {}", id, stockDTO.getMedicineId());
        Optional<Stock> optionalStock = stockRepository.findById(id);
        if (optionalStock.isPresent()) {
            Stock existingStock = optionalStock.get();

            // Check if medicine is being changed and if it's valid
            if (!existingStock.getMedicine().getItemId().equals(stockDTO.getMedicineId())) {
                Optional<Medicine> newMedicine = medicineRepository.findById(stockDTO.getMedicineId());
                if (!newMedicine.isPresent()) {
                    log.error("New Medicine with ID {} not found. Cannot update stock.", stockDTO.getMedicineId());
                    return null; // Or throw
                }
                existingStock.setMedicine(newMedicine.get());
            }

            existingStock.setQuantity(stockDTO.getQuantity());
            existingStock.setPrice(stockDTO.getPrice());
            existingStock.setExpiryDate(stockDTO.getExpiryDate());
            existingStock.setManufacturingDate(stockDTO.getManufacturingDate());
            // Timestamps managed by @PreUpdate
            return stockMapper.toDTO(stockRepository.save(existingStock));
        } else {
            log.warn("Stock with ID {} not found for update", id);
            return null;
        }
    }

    @Override
    @Transactional
    public void deleteStock(Long id) {
        log.info("Soft deleting stock with ID: {}", id);
        Optional<Stock> optionalStock = stockRepository.findById(id);
        if (optionalStock.isPresent()) {
            Stock stock = optionalStock.get();
            stock.setSoftDelete(true);
            stockRepository.save(stock);
        } else {
            log.warn("Stock with ID {} not found for deletion", id);
        }
    }
}
