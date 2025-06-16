package com.pharmacy.management.services.impl;

import com.pharmacy.management.dto.SaleDTO;
import com.pharmacy.management.dto.SalesItemDTO;
import com.pharmacy.management.mapper.SaleMapper;
import com.pharmacy.management.mapper.SalesItemMapper;
import com.pharmacy.management.models.*;
import com.pharmacy.management.repositories.*;
import com.pharmacy.management.services.SaleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SaleServiceImpl implements SaleService {

    private final SaleRepository saleRepository;
    private final SaleMapper saleMapper;
    private final SalesItemMapper salesItemMapper;
    private final MedicineRepository medicineRepository;
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    // private final StockRepository stockRepository; // For stock deduction later

    @Autowired
    public SaleServiceImpl(SaleRepository saleRepository, SaleMapper saleMapper,
                           SalesItemMapper salesItemMapper, MedicineRepository medicineRepository,
                           UserRepository userRepository, CustomerRepository customerRepository) {
        this.saleRepository = saleRepository;
        this.saleMapper = saleMapper;
        this.salesItemMapper = salesItemMapper;
        this.medicineRepository = medicineRepository;
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public SaleDTO getSaleById(Long id) {
        log.info("Fetching sale with ID: {}", id);
        return saleRepository.findById(id).map(saleMapper::toDTO).orElse(null);
    }

    @Override
    public List<SaleDTO> getAllSales() {
        log.info("Fetching all sales");
        return saleRepository.findAll().stream().map(saleMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SaleDTO createSale(SaleDTO saleDTO) {
        log.info("Creating new sale");
        Sale sale = saleMapper.toEntity(saleDTO);

        // Fetch and set User if userId is provided
        if (saleDTO.getUserId() != null) {
            userRepository.findById(saleDTO.getUserId()).ifPresent(sale::setUser);
        }
        // Fetch and set Customer if customerId is provided
        if (saleDTO.getCustomerId() != null) {
            customerRepository.findById(saleDTO.getCustomerId()).ifPresent(sale::setCustomer);
        }

        BigDecimal calculatedTotalAmount = BigDecimal.ZERO;
        sale.getSalesItems().clear(); // Clear any mapped shells, rebuild from DTO items

        for (SalesItemDTO itemDTO : saleDTO.getItems()) {
            Optional<Medicine> medOpt = medicineRepository.findById(itemDTO.getMedicineId());
            if (!medOpt.isPresent()) {
                log.error("Medicine with ID {} not found. Sale creation aborted.", itemDTO.getMedicineId());
                throw new IllegalArgumentException("Medicine not found: " + itemDTO.getMedicineId());
            }
            Medicine medicine = medOpt.get();

            // TODO: Stock check and deduction would happen here
            // if (stockRepository.getAvailableStock(medicine.getItemId()) < itemDTO.getQuantity()) {
            //    throw new InsufficientStockException("Not enough stock for " + medicine.getName());
            // }

            SalesItem salesItem = salesItemMapper.toEntity(itemDTO);
            salesItem.setMedicine(medicine);
            // Use price from DTO or fetch from Medicine master? For now, DTO price.
            // salesItem.setPricePerUnit(medicine.getUomPrice()); // Example if using master price
            salesItem.setPricePerUnit(itemDTO.getPricePerUnit());


            BigDecimal itemTotal = salesItem.getPricePerUnit().multiply(BigDecimal.valueOf(salesItem.getQuantity()));
            calculatedTotalAmount = calculatedTotalAmount.add(itemTotal);

            sale.addSalesItem(salesItem); // This sets the bidirectional link
        }
        sale.setTotalAmount(calculatedTotalAmount);
        // sale.setSaleDate(new Timestamp(System.currentTimeMillis())); // Or rely on @PrePersist / DB default

        Sale savedSale = saleRepository.save(sale);
        log.info("Sale created with ID: {}", savedSale.getSaleId());
        return saleMapper.toDTO(savedSale);
    }

    @Override
    @Transactional
    public void deleteSale(Long id) {
        log.info("Soft deleting sale with ID: {}", id);
        Optional<Sale> optionalSale = saleRepository.findById(id);
        if (optionalSale.isPresent()) {
            Sale sale = optionalSale.get();
            sale.setSoftDelete(true);
            // Soft delete SalesItems associated with this Sale
            // The @Where clause on SalesItem handles filtering, but explicitly marking them is cleaner
            // Or rely on CascadeType.PERSIST/MERGE + manual soft delete of children if not using ALL
            // For now, @OneToMany with CascadeType.ALL should manage this if Sale entity graph is saved.
            // However, explicit soft deletion of children is safer for @Where clauses.
            sale.getSalesItems().forEach(item -> item.setSoftDelete(true));
            saleRepository.save(sale);
        } else {
            log.warn("Sale with ID {} not found for deletion", id);
        }
    }
}
