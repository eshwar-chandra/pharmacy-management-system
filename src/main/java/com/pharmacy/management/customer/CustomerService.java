package com.pharmacy.management.customer; // Updated package

import com.pharmacy.management.customer.CustomerDTO; // Updated import
import java.util.List;

public interface CustomerService {
    CustomerDTO getCustomerById(Long id);
    List<CustomerDTO> getAllCustomers();
    CustomerDTO addCustomer(CustomerDTO customerDTO);
    CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO);
    void deleteCustomer(Long id); // Soft delete
}
