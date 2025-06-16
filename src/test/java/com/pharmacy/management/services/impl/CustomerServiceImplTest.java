package com.pharmacy.management.services.impl;

import com.pharmacy.management.dto.CustomerDTO;
import com.pharmacy.management.mapper.CustomerMapper;
import com.pharmacy.management.models.Customer;
import com.pharmacy.management.repositories.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Customer customer;
    private CustomerDTO customerDTO;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setCustomerId(1L);
        customer.setName("Test Customer");
        customer.setPhoneNumber("1234567890");
        customer.setEmail("test@example.com");
        customer.setSoftDelete(false);

        customerDTO = new CustomerDTO();
        customerDTO.setId(1L);
        customerDTO.setName("Test Customer DTO");
        customerDTO.setPhoneNumber("1234567890");
        customerDTO.setEmail("testdto@example.com");
    }

    @Test
    void getCustomerById_found() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerMapper.toDTO(customer)).thenReturn(customerDTO);

        CustomerDTO result = customerService.getCustomerById(1L);

        assertNotNull(result);
        assertEquals(customerDTO.getName(), result.getName());
        verify(customerRepository).findById(1L);
        verify(customerMapper).toDTO(customer);
    }

    @Test
    void getCustomerById_notFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());
        CustomerDTO result = customerService.getCustomerById(1L);
        assertNull(result);
        verify(customerRepository).findById(1L);
    }

    @Test
    void getAllCustomers_success() {
        when(customerRepository.findAll()).thenReturn(Collections.singletonList(customer));
        when(customerMapper.toDTO(customer)).thenReturn(customerDTO);

        List<CustomerDTO> results = customerService.getAllCustomers();

        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        assertEquals(customerDTO.getName(), results.get(0).getName());
        verify(customerRepository).findAll();
    }

    @Test
    void getAllCustomers_empty() {
        when(customerRepository.findAll()).thenReturn(Collections.emptyList());
        List<CustomerDTO> results = customerService.getAllCustomers();
        assertNotNull(results);
        assertTrue(results.isEmpty());
        verify(customerRepository).findAll();
    }

    @Test
    void addCustomer_success() {
        when(customerMapper.toEntity(customerDTO)).thenReturn(customer);
        when(customerRepository.save(customer)).thenReturn(customer);
        when(customerMapper.toDTO(customer)).thenReturn(customerDTO);

        CustomerDTO result = customerService.addCustomer(customerDTO);

        assertNotNull(result);
        assertEquals(customerDTO.getName(), result.getName());
        verify(customerRepository).save(customer);
    }

    @Test
    void updateCustomer_found() {
        CustomerDTO updatedDto = new CustomerDTO();
        updatedDto.setId(1L);
        updatedDto.setName("Updated Name");
        updatedDto.setEmail("updated@example.com");

        Customer existingCustomer = new Customer();
        existingCustomer.setCustomerId(1L);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(existingCustomer));
        when(customerRepository.save(any(Customer.class))).thenAnswer(inv -> inv.getArgument(0));
        when(customerMapper.toDTO(any(Customer.class))).thenAnswer(inv -> {
            Customer c = inv.getArgument(0);
            CustomerDTO dto = new CustomerDTO();
            dto.setId(c.getCustomerId());
            dto.setName(c.getName());
            dto.setEmail(c.getEmail());
            return dto;
        });


        CustomerDTO result = customerService.updateCustomer(1L, updatedDto);

        assertNotNull(result);
        assertEquals("Updated Name", result.getName());
        assertEquals("updated@example.com", result.getEmail());
        verify(customerRepository).findById(1L);
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    void updateCustomer_notFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());
        CustomerDTO result = customerService.updateCustomer(1L, customerDTO);
        assertNull(result);
        verify(customerRepository).findById(1L);
        verify(customerRepository, never()).save(any());
    }

    @Test
    void deleteCustomer_softDeleteSuccess() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerRepository.save(customer)).thenReturn(customer);

        customerService.deleteCustomer(1L);

        assertTrue(customer.isSoftDelete());
        verify(customerRepository).findById(1L);
        verify(customerRepository).save(customer);
    }

    @Test
    void deleteCustomer_notFound() {
       when(customerRepository.findById(1L)).thenReturn(Optional.empty());
       customerService.deleteCustomer(1L);
       verify(customerRepository).findById(1L);
       verify(customerRepository, never()).save(any());
    }
}
