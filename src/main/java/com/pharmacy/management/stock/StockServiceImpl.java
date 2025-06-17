package com.pharmacy.management.stock; // Updated package

import com.pharmacy.management.common.exception.BadRequestException; // Updated import
import com.pharmacy.management.common.exception.ResourceNotFoundException; // Updated import
import com.pharmacy.management.medicine.Medicine; // Updated import
import com.pharmacy.management.medicine.MedicineRepository; // Updated import
import com.pharmacy.management.stock.StockDTO; // Updated import
import com.pharmacy.management.stock.StockMapper; // Updated import
import com.pharmacy.management.stock.Stock; // Updated import
import com.pharmacy.management.stock.StockRepository; // Updated import
import com.pharmacy.management.stock.StockService; // Updated import
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
// import java.util.Optional; // No longer needed directly due to orElseThrow
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;
    private final StockMapper stockMapper;
    private final MedicineRepository medicineRepository;

    @Override
    public StockDTO getStockById(Long id) {
        log.info("Fetching stock with ID: {}", id);
        return stockRepository.findById(id)
                .map(stockMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Stock not found with ID: " + id));
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
        Medicine medicine = medicineRepository.findById(stockDTO.getMedicineId())
                .orElseThrow(() -> new BadRequestException("Medicine not found with ID: " + stockDTO.getMedicineId() + ". Cannot add stock."));

        stock.setMedicine(medicine);
        return stockMapper.toDTO(stockRepository.save(stock));
    }

    @Override
    @Transactional
    public StockDTO updateStock(Long id, StockDTO stockDTO) {
        log.info("Updating stock with ID {}: for medicine ID {}", id, stockDTO.getMedicineId());
        Stock existingStock = stockRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stock not found with ID: " + id));

        if (stockDTO.getMedicineId() != null && !existingStock.getMedicine().getItemId().equals(stockDTO.getMedicineId())) {
            Medicine newMedicine = medicineRepository.findById(stockDTO.getMedicineId())
                    .orElseThrow(() -> new BadRequestException("New Medicine with ID: " + stockDTO.getMedicineId() + " not found. Cannot update stock."));
            existingStock.setMedicine(newMedicine);
        }

        existingStock.setQuantity(stockDTO.getQuantity());
        existingStock.setPrice(stockDTO.getPrice());
        existingStock.setExpiryDate(stockDTO.getExpiryDate());
        existingStock.setManufacturingDate(stockDTO.getManufacturingDate());
        return stockMapper.toDTO(stockRepository.save(existingStock));
    }

    @Override
    @Transactional
    public void deleteStock(Long id) {
        log.info("Soft deleting stock with ID: {}", id);
        Stock stock = stockRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stock not found with ID: " + id));
        stock.setSoftDelete(true);
        stockRepository.save(stock);
    }
}
