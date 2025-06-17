package com.pharmacy.management.medicine; // Updated package

import com.pharmacy.management.medicine.MedicineDTO; // Updated import
import java.util.List;

public interface MedicineService {

    MedicineDTO getMedicineById(Long id);

    List<MedicineDTO> getAllMedicines();

    MedicineDTO addMedicine(MedicineDTO medicineDTO);

    MedicineDTO updateMedicine(Long id, MedicineDTO updatedMedicineDTO);

    void deleteMedicine(Long id);
}
