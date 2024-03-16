package com.pharmacy.management.services;

import com.pharmacy.management.dto.MedicineDTO;
import java.util.List;

public interface MedicineService {

    MedicineDTO getMedicineById(Long id);

    List<MedicineDTO> getAllMedicines();

    MedicineDTO addMedicine(MedicineDTO medicineDTO);

    MedicineDTO updateMedicine(Long id, MedicineDTO updatedMedicineDTO);

    void deleteMedicine(Long id);
}

