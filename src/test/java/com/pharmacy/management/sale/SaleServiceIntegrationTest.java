package com.pharmacy.management.sale;

import com.pharmacy.management.AbstractIntegrationTest;
import com.pharmacy.management.customer.Customer;
import com.pharmacy.management.customer.CustomerRepository;
import com.pharmacy.management.medicine.Medicine;
import com.pharmacy.management.medicine.MedicineRepository;
import com.pharmacy.management.user.User;
import com.pharmacy.management.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;

class SaleServiceIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private SaleService saleService;

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private MedicineRepository medicineRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomerRepository customerRepository;

    private Medicine testMedicine;
    private User testUser;
    private Customer testCustomer;

    @BeforeEach
    void setUpPrerequisites() {
        // Clean up is implicitly handled by ddl-auto: create wiping and recreating schema for each test class
        // If tests were in the same class and not @Transactional, manual cleanup might be needed for some scenarios.

        testMedicine = Medicine.builder()
            .name("Test Med for Sale")
            .manufacturerName("Pharma Co")
            .uniqueIdentifier("SALEITM001" + System.nanoTime()) // Ensure unique for each run if not fully cleaning
            .sellUom(1)
            .uomPrice(BigDecimal.valueOf(10.00))
            .packageSize(10)
            .softDelete(false) // Explicitly set non-default if builder doesn't
            .build();
        medicineRepository.save(testMedicine);

        testUser = User.builder()
            .username("saleuser" + System.nanoTime()) // Ensure unique
            .password("password123")
            .email("saleuser" + System.nanoTime() + "@example.com") // Ensure unique
            .softDelete(false)
            .build();
        userRepository.save(testUser);

        testCustomer = Customer.builder()
            .name("Sale Customer")
            .email("salecust" + System.nanoTime() + "@example.com") // Ensure unique
            .phoneNumber("1231231234")
            .softDelete(false)
            .build();
        customerRepository.save(testCustomer);
    }

    @Test
    @Transactional
    void createSale_success() {
        SalesItemDTO salesItemDTO = SalesItemDTO.builder()
            .medicineId(testMedicine.getItemId())
            .quantity(2)
            .pricePerUnit(BigDecimal.valueOf(10.00))
            .build();

        SaleDTO saleDTOToCreate = SaleDTO.builder()
            .userId(testUser.getUserId())
            .customerId(testCustomer.getCustomerId())
            .items(Collections.singletonList(salesItemDTO))
            .build();

        SaleDTO createdSale = saleService.createSale(saleDTOToCreate);

        assertNotNull(createdSale);
        assertNotNull(createdSale.getId());
        assertEquals(testUser.getUserId(), createdSale.getUserId());
        assertEquals(testCustomer.getCustomerId(), createdSale.getCustomerId());
        assertNotNull(createdSale.getItems());
        assertEquals(1, createdSale.getItems().size());
        assertEquals(testMedicine.getItemId(), createdSale.getItems().get(0).getMedicineId());
        assertEquals(2, createdSale.getItems().get(0).getQuantity());

        assertEquals(0, BigDecimal.valueOf(20.00).compareTo(createdSale.getTotalAmount()), "Total amount should be 20.00");

        Sale persistedSale = saleRepository.findById(createdSale.getId()).orElse(null);
        assertNotNull(persistedSale);
        assertEquals(1, persistedSale.getSalesItems().size());
        assertEquals(testMedicine.getItemId(), persistedSale.getSalesItems().get(0).getMedicine().getItemId());
    }

    @Test
    @Transactional
    void createSale_medicineNotFound_throwsBadRequestException() {
        SalesItemDTO salesItemDTO = SalesItemDTO.builder()
            .medicineId(9999L) // Non-existent medicine ID
            .quantity(1)
            .pricePerUnit(BigDecimal.valueOf(5.00))
            .build();

        SaleDTO saleDTOToCreate = SaleDTO.builder()
            .userId(testUser.getUserId())
            .customerId(testCustomer.getCustomerId())
            .items(Collections.singletonList(salesItemDTO))
            .build();

        assertThrows(com.pharmacy.management.common.exception.BadRequestException.class, () -> {
            saleService.createSale(saleDTOToCreate);
        });
    }
}
