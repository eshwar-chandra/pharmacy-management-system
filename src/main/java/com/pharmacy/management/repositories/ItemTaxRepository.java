package com.pharmacy.management.repositories;

import com.pharmacy.management.models.ItemTax;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemTaxRepository extends JpaRepository<ItemTax, Long> {
}