package com.pharmacy.management.repositories;

import com.pharmacy.management.models.StockReturn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockReturnRepository extends JpaRepository<StockReturn, Long> {}
