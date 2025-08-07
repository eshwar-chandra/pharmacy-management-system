package com.pharmacy.management.customer; // Updated package

import com.pharmacy.management.customer.Customer; // Updated import
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    // Custom query methods if needed in the future
}
