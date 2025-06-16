package com.pharmacy.management.services.impl;

import com.pharmacy.management.dto.StockDTO;
import com.pharmacy.management.mapper.StockMapper;
import com.pharmacy.management.models.Medicine;
import com.pharmacy.management.models.Stock;
import com.pharmacy.management.repositories.MedicineRepository;
import com.pharmacy.management.repositories.StockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockServiceImplTest {

    @Mock
    private StockRepository stockRepository;

    @Mock
    private StockMapper stockMapper;

    @Mock
    private MedicineRepository medicineRepository;

    @InjectMocks
    private StockServiceImpl stockService;

    private Stock stock;
    private StockDTO stockDTO;
    private Medicine medicine;

    @BeforeEach
    void setUp() {
        medicine = new Medicine();
        medicine.setItemId(1L);
        medicine.setName("TestMedicine");
        medicine.setUomPrice(BigDecimal.valueOf(10));

        stock = new Stock();
        stock.setStockId(1L);
        stock.setMedicine(medicine);
        stock.setQuantity(100);
        stock.setPrice(BigDecimal.valueOf(12));
        stock.setSoftDelete(false);

        stockDTO = new StockDTO();
        stockDTO.setId(1L);
        stockDTO.setMedicineId(1L);
        stockDTO.setMedicineName("TestMedicineDTO");
        stockDTO.setQuantity(100);
        stockDTO.setPrice(BigDecimal.valueOf(12));
    }

    @Test
    void getStockById_found() {
        when(stockRepository.findById(1L)).thenReturn(Optional.of(stock));
        when(stockMapper.toDTO(stock)).thenReturn(stockDTO);

        StockDTO result = stockService.getStockById(1L);

        assertNotNull(result);
        assertEquals(stockDTO.getMedicineName(), result.getMedicineName());
        verify(stockRepository).findById(1L);
    }

    @Test
    void getStockById_notFound() {
        when(stockRepository.findById(1L)).thenReturn(Optional.empty());
        StockDTO result = stockService.getStockById(1L);
        assertNull(result);
        verify(stockRepository).findById(1L);
    }

    @Test
    void getAllStock_success() {
        when(stockRepository.findAll()).thenReturn(Collections.singletonList(stock));
        when(stockMapper.toDTO(stock)).thenReturn(stockDTO);

        List<StockDTO> results = stockService.getAllStock();

        assertNotNull(results);
        assertEquals(1, results.size());
        verify(stockRepository).findAll();
    }

    @Test
    void addStock_success() {
        when(medicineRepository.findById(1L)).thenReturn(Optional.of(medicine));
        when(stockMapper.toEntity(stockDTO)).thenReturn(stock); // map DTO to entity shell
        when(stockRepository.save(any(Stock.class))).thenReturn(stock); // save returns full entity
        when(stockMapper.toDTO(stock)).thenReturn(stockDTO); // map full entity to DTO

        StockDTO result = stockService.addStock(stockDTO);

        assertNotNull(result);
        assertEquals(stockDTO.getMedicineName(), result.getMedicineName());
        verify(medicineRepository).findById(1L);
        verify(stockRepository).save(any(Stock.class));
    }

    @Test
    void addStock_medicineNotFound() {
        when(medicineRepository.findById(1L)).thenReturn(Optional.empty());
         when(stockMapper.toEntity(stockDTO)).thenReturn(stock);


        StockDTO result = stockService.addStock(stockDTO);
        assertNull(result); // Or assert exception if service throws one
        verify(medicineRepository).findById(1L);
        verify(stockRepository, never()).save(any());
    }

    @Test
    void updateStock_success() {
        StockDTO updatedDto = new StockDTO();
        updatedDto.setId(1L);
        updatedDto.setMedicineId(1L); // Assuming medicine ID doesn't change or is validated
        updatedDto.setQuantity(150);
        updatedDto.setPrice(BigDecimal.valueOf(15));

        Stock existingStock = new Stock();
        existingStock.setStockId(1L);
        existingStock.setMedicine(medicine); // Link to existing medicine

        when(stockRepository.findById(1L)).thenReturn(Optional.of(existingStock));
        when(medicineRepository.findById(1L)).thenReturn(Optional.of(medicine)); // If medicineId can change
        when(stockRepository.save(any(Stock.class))).thenAnswer(inv -> inv.getArgument(0));
        when(stockMapper.toDTO(any(Stock.class))).thenAnswer(inv -> {
            Stock s = inv.getArgument(0);
            StockDTO dto = new StockDTO();
            dto.setId(s.getStockId());
            dto.setQuantity(s.getQuantity());
            dto.setPrice(s.getPrice());
            dto.setMedicineId(s.getMedicine().getItemId());
            return dto;
        });

        StockDTO result = stockService.updateStock(1L, updatedDto);

        assertNotNull(result);
        assertEquals(150, result.getQuantity());
        assertEquals(0, BigDecimal.valueOf(15).compareTo(result.getPrice()));
        verify(stockRepository).findById(1L);
        verify(stockRepository).save(any(Stock.class));
    }


    @Test
    void updateStock_notFound() {
        when(stockRepository.findById(1L)).thenReturn(Optional.empty());
        StockDTO result = stockService.updateStock(1L, stockDTO);
        assertNull(result);
        verify(stockRepository).findById(1L);
        verify(stockRepository, never()).save(any());
    }

    @Test
    void updateStock_newMedicineNotFound() {
        stockDTO.setMedicineId(2L); // Try to update to a non-existent medicine
        Stock existingStock = new Stock(); // need a separate instance for the test
        existingStock.setStockId(1L);
        existingStock.setMedicine(medicine); // Original medicine

        when(stockRepository.findById(1L)).thenReturn(Optional.of(existingStock));
        when(medicineRepository.findById(2L)).thenReturn(Optional.empty()); // New medicine not found

        StockDTO result = stockService.updateStock(1L, stockDTO);

        assertNull(result);
        verify(stockRepository).findById(1L);
        verify(medicineRepository).findById(2L);
        verify(stockRepository, never()).save(any());
    }


    @Test
    void deleteStock_softDeleteSuccess() {
        when(stockRepository.findById(1L)).thenReturn(Optional.of(stock));
        when(stockRepository.save(stock)).thenReturn(stock);

        stockService.deleteStock(1L);

        assertTrue(stock.isSoftDelete());
        verify(stockRepository).findById(1L);
        verify(stockRepository).save(stock);
    }

    @Test
    void deleteStock_notFound() {
       when(stockRepository.findById(1L)).thenReturn(Optional.empty());
       stockService.deleteStock(1L);
       verify(stockRepository).findById(1L);
       verify(stockRepository, never()).save(any());
    }
}
