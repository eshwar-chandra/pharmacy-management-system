package com.pharmacy.management.medicine; // Updated package

import com.pharmacy.management.medicine.Medicine; // Updated import
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Removed unused import java.util.List;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long> {
    // You can define custom query methods here if needed
}
