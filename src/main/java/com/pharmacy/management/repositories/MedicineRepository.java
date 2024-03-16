package com.pharmacy.management.repositories;

import com.pharmacy.management.models.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long> {
    // You can define custom query methods here if needed
}

