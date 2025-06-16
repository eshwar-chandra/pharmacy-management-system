package com.pharmacy.management.services;

import com.pharmacy.management.dto.CustomerDTO;
import java.util.List;

public interface CustomerService {
    CustomerDTO getCustomerById(Long id);
    List<CustomerDTO> getAllCustomers();
    CustomerDTO addCustomer(CustomerDTO customerDTO);
    CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO);
    void deleteCustomer(Long id); // Soft delete
}
