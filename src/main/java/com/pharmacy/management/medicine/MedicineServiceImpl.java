package com.pharmacy.management.medicine; // Updated package

import com.pharmacy.management.common.exception.ResourceNotFoundException; // Updated import
import com.pharmacy.management.medicine.MedicineDTO; // Updated import
import com.pharmacy.management.medicine.MedicineMapper; // Updated import
import com.pharmacy.management.medicine.Medicine; // Updated import
import com.pharmacy.management.medicine.MedicineRepository; // Updated import
import com.pharmacy.management.medicine.MedicineService; // Updated import
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
// Removed unused import java.util.Optional;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
@RequiredArgsConstructor
public class MedicineServiceImpl implements MedicineService {

    private final MedicineRepository medicineRepository;
    private final MedicineMapper medicineMapper;

    @Override
    @Cacheable(value = "medicineCache", key = "#id")
    public MedicineDTO getMedicineById(Long id) {
        log.info("Fetching medicine with ID: {}", id);
        return medicineRepository.findById(id)
                .map(medicineMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found with ID: " + id));
    }

    @Override
    public List<MedicineDTO> getAllMedicines() {
        log.info("Fetching all medicines");
        List<Medicine> medicines = medicineRepository.findAll();
        return medicines.stream()
                .map(medicineMapper::toDTO)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public MedicineDTO addMedicine(MedicineDTO medicineDTO) {
        log.info("Adding new medicine: {}", medicineDTO);
        Medicine medicine = medicineMapper.toEntity(medicineDTO);
        return medicineMapper.toDTO(medicineRepository.save(medicine));
    }

    @Override
    @CachePut(value = "medicineCache", key = "#id")
    public MedicineDTO updateMedicine(Long id, MedicineDTO updatedMedicineDTO) {
        log.info("Updating medicine with ID {}: {}", id, updatedMedicineDTO);
        Medicine existingMedicine = medicineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found with ID: " + id));

        Medicine updatedMedicineEntity = medicineMapper.toEntity(updatedMedicineDTO);
        updatedMedicineEntity.setItemId(existingMedicine.getItemId());
        updatedMedicineEntity.setCreatedAt(existingMedicine.getCreatedAt());

        existingMedicine.setName(updatedMedicineEntity.getName());
        existingMedicine.setManufacturerName(updatedMedicineEntity.getManufacturerName());
        existingMedicine.setGenericName(updatedMedicineEntity.getGenericName());
        existingMedicine.setSellUom(updatedMedicineEntity.getSellUom());
        existingMedicine.setUomPrice(updatedMedicineEntity.getUomPrice());
        existingMedicine.setPackageSize(updatedMedicineEntity.getPackageSize());
        existingMedicine.setUniqueIdentifier(updatedMedicineEntity.getUniqueIdentifier());
        existingMedicine.setSoftDelete(updatedMedicineEntity.isSoftDelete());
        return medicineMapper.toDTO(medicineRepository.save(existingMedicine));
    }

    @Override
    @CacheEvict(value = "medicineCache", key = "#id")
    public void deleteMedicine(Long id) {
        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found with ID: " + id));
        medicine.setSoftDelete(true);
        medicineRepository.save(medicine);
        log.info("Soft deleting medicine with ID: {}", id);
    }
}
