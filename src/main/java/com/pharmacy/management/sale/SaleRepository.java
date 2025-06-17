package com.pharmacy.management.sale; // Updated package

import com.pharmacy.management.sale.Sale; // Updated import
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {}
