package com.pharmacy.management.stock; // Updated package

import com.pharmacy.management.stock.Stock; // Updated import
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    // Future custom queries
}
