package com.pharmacy.management.sale; // Updated package

import com.pharmacy.management.common.exception.BadRequestException; // Updated import
import com.pharmacy.management.common.exception.ResourceNotFoundException; // Updated import
import com.pharmacy.management.customer.CustomerRepository; // Updated import
import com.pharmacy.management.medicine.Medicine; // Updated import
import com.pharmacy.management.medicine.MedicineRepository; // Updated import
import com.pharmacy.management.sale.SaleDTO; // Updated import
import com.pharmacy.management.sale.SalesItemDTO; // Updated import
import com.pharmacy.management.sale.SaleMapper; // Updated import
import com.pharmacy.management.sale.SalesItemMapper; // Updated import
import com.pharmacy.management.sale.Sale; // Updated import
import com.pharmacy.management.sale.SalesItem; // Updated import
import com.pharmacy.management.sale.SaleRepository; // Updated import
// SalesItemRepository is not directly used by SaleServiceImpl in the provided code
import com.pharmacy.management.sale.SaleService; // Updated import
import com.pharmacy.management.sale.strategy.PostSaleActionStrategy; // Import for the strategy
import com.pharmacy.management.user.UserRepository; // Updated import

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
// import java.util.Optional; // No longer directly used
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SaleServiceImpl implements SaleService {

    private final SaleRepository saleRepository;
    private final SaleMapper saleMapper;
    private final SalesItemMapper salesItemMapper;
    private final MedicineRepository medicineRepository;
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final List<PostSaleActionStrategy> postSaleActionStrategies; // Added for strategy pattern
    // private final StockRepository stockRepository; // For stock deduction later

    @Override
    public SaleDTO getSaleById(Long id) {
        log.info("Fetching sale with ID: {}", id);
        return saleRepository.findById(id)
                .map(saleMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Sale not found with ID: " + id));
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

        if (saleDTO.getUserId() != null) {
            userRepository.findById(saleDTO.getUserId()).ifPresent(sale::setUser);
            // Consider throwing BadRequestException if user not found, or ifPresentOrElse
        }
        if (saleDTO.getCustomerId() != null) {
            customerRepository.findById(saleDTO.getCustomerId()).ifPresent(sale::setCustomer);
            // Consider throwing BadRequestException if customer not found
        }

        BigDecimal calculatedTotalAmount = BigDecimal.ZERO;
        sale.getSalesItems().clear();

        for (SalesItemDTO itemDTO : saleDTO.getItems()) {
            Medicine medicine = medicineRepository.findById(itemDTO.getMedicineId())
                    .orElseThrow(() -> new BadRequestException("Medicine with ID " + itemDTO.getMedicineId() + " not found. Sale creation aborted."));

            SalesItem salesItem = salesItemMapper.toEntity(itemDTO);
            salesItem.setMedicine(medicine);
            salesItem.setPricePerUnit(itemDTO.getPricePerUnit());

            BigDecimal itemTotal = salesItem.getPricePerUnit().multiply(BigDecimal.valueOf(salesItem.getQuantity()));
            calculatedTotalAmount = calculatedTotalAmount.add(itemTotal);

            sale.addSalesItem(salesItem);
        }
        sale.setTotalAmount(calculatedTotalAmount);

        Sale savedSale = saleRepository.save(sale);
        log.info("Sale created with ID: {}", savedSale.getSaleId());

        // **** Execute post-sale action strategies ****
        final Sale finalSavedSale = savedSale;
        if (postSaleActionStrategies != null && !postSaleActionStrategies.isEmpty()) {
            log.info("Executing post-sale actions for Sale ID: {}", finalSavedSale.getSaleId());
            for (PostSaleActionStrategy strategy : postSaleActionStrategies) {
                try {
                    strategy.execute(finalSavedSale);
                } catch (Exception e) {
                    // Log error but don't let strategy failure roll back sale creation
                    log.error("Error executing post-sale action {} for Sale ID: {}",
                              strategy.getClass().getSimpleName(), finalSavedSale.getSaleId(), e);
                }
            }
        }
        // **** End of new block ****

        return saleMapper.toDTO(savedSale);
    }

    @Override
    @Transactional
    public void deleteSale(Long id) {
        log.info("Soft deleting sale with ID: {}", id);
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sale not found with ID: " + id));

        sale.setSoftDelete(true);
        sale.getSalesItems().forEach(item -> item.setSoftDelete(true));
        saleRepository.save(sale);
    }
}
