package com.pharmacy.management.services.impl;

import com.pharmacy.management.dto.CustomerDTO;
import com.pharmacy.management.mapper.CustomerMapper;
import com.pharmacy.management.models.Customer;
import com.pharmacy.management.repositories.CustomerRepository;
import com.pharmacy.management.services.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    @Override
    public CustomerDTO getCustomerById(Long id) {
        log.info("Fetching customer with ID: {}", id);
        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        return optionalCustomer.map(customerMapper::toDTO).orElse(null);
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
    public CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO) {
        log.info("Updating customer with ID {}: {}", id, customerDTO.getName());
        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        if (optionalCustomer.isPresent()) {
            Customer existingCustomer = optionalCustomer.get();
            existingCustomer.setName(customerDTO.getName());
            existingCustomer.setPhoneNumber(customerDTO.getPhoneNumber());
            existingCustomer.setEmail(customerDTO.getEmail());
            // Timestamps managed by @PreUpdate
            return customerMapper.toDTO(customerRepository.save(existingCustomer));
        } else {
            log.warn("Customer with ID {} not found for update", id);
            return null;
        }
    }

    @Override
    @Transactional
    public void deleteCustomer(Long id) {
        log.info("Soft deleting customer with ID: {}", id);
        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        if (optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();
            customer.setSoftDelete(true);
            customerRepository.save(customer);
        } else {
            log.warn("Customer with ID {} not found for deletion", id);
        }
    }
}
