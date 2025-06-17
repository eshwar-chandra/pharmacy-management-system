package com.pharmacy.management.stockreturn; // Updated package

import com.pharmacy.management.stockreturn.StockReturn; // Updated import
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockReturnRepository extends JpaRepository<StockReturn, Long> {}
