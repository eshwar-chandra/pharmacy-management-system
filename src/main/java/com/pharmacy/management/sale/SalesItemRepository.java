package com.pharmacy.management.sale; // Updated package

import com.pharmacy.management.sale.SalesItem; // Updated import
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesItemRepository extends JpaRepository<SalesItem, Long> {}
