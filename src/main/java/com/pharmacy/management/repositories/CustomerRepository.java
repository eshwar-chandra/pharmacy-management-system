package com.pharmacy.management.repositories;

import com.pharmacy.management.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    // Custom query methods if needed in the future
}
