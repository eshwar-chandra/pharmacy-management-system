package com.pharmacy.management.customer; // Updated package

import com.pharmacy.management.common.exception.ResourceNotFoundException; // Updated import
import com.pharmacy.management.customer.CustomerDTO; // Updated import
import com.pharmacy.management.customer.CustomerMapper; // Updated import
import com.pharmacy.management.customer.Customer; // Updated import
import com.pharmacy.management.customer.CustomerRepository; // Updated import
import com.pharmacy.management.customer.CustomerService; // Updated import
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
// Removed java.util.Optional as it's not directly used after refactoring to orElseThrow
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    @Cacheable(value = "customerCache", key = "#id")
    public CustomerDTO getCustomerById(Long id) {
        log.info("Fetching customer with ID: {}", id);
        return customerRepository.findById(id)
                .map(customerMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + id));
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        log.info("Fetching all customers");
        return customerRepository.findAll().stream()
                .map(customerMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CustomerDTO addCustomer(CustomerDTO customerDTO) {
        log.info("Adding new customer: {}", customerDTO.getName());
        Customer customer = customerMapper.toEntity(customerDTO);
        return customerMapper.toDTO(customerRepository.save(customer));
    }

    @Override
    @Transactional
    @CachePut(value = "customerCache", key = "#id")
    public CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO) {
        log.info("Updating customer with ID {}: {}", id, customerDTO.getName());
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + id));

        existingCustomer.setName(customerDTO.getName());
        existingCustomer.setPhoneNumber(customerDTO.getPhoneNumber());
        existingCustomer.setEmail(customerDTO.getEmail());
        // Timestamps managed by @PreUpdate in Customer entity
        return customerMapper.toDTO(customerRepository.save(existingCustomer));
    }

    @Override
    @Transactional
    @CacheEvict(value = "customerCache", key = "#id")
    public void deleteCustomer(Long id) {
        log.info("Soft deleting customer with ID: {}", id);
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + id));
        customer.setSoftDelete(true);
        customerRepository.save(customer);
    }
}
