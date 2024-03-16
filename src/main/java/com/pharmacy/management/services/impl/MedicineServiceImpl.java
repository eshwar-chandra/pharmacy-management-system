package com.pharmacy.management.services.impl;

import com.pharmacy.management.dto.MedicineDTO;
import com.pharmacy.management.mapper.MedicineMapper;
import com.pharmacy.management.models.Medicine;
import com.pharmacy.management.repositories.MedicineRepository;
import com.pharmacy.management.services.MedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class MedicineServiceImpl implements MedicineService {

    private final MedicineRepository medicineRepository;
    private final MedicineMapper medicineMapper;

    @Autowired
    public MedicineServiceImpl(MedicineRepository medicineRepository, MedicineMapper medicineMapper) {
        this.medicineRepository = medicineRepository;
        this.medicineMapper = medicineMapper;
    }

    @Override
    public MedicineDTO getMedicineById(Long id) {
        log.info("Fetching medicine with ID: {}", id);
        Optional<Medicine> optionalMedicine = medicineRepository.findById(id);
        return optionalMedicine.map(medicineMapper::toDTO).orElse(null);
    }

    @Override
    public List<MedicineDTO> getAllMedicines() {
        return null;
    }

    @Override
    public MedicineDTO addMedicine(MedicineDTO medicineDTO) {
        log.info("Adding new medicine: {}", medicineDTO);
        Medicine medicine = medicineMapper.toEntity(medicineDTO);
        return medicineMapper.toDTO(medicineRepository.save(medicine));
    }

    @Override
    public MedicineDTO updateMedicine(Long id, MedicineDTO updatedMedicineDTO) {
        log.info("Updating medicine with ID {}: {}", id, updatedMedicineDTO);
        Optional<Medicine> optionalMedicine = medicineRepository.findById(id);
        if (optionalMedicine.isPresent()) {
            Medicine existingMedicine = optionalMedicine.get();
            Medicine updatedMedicine = medicineMapper.toEntity(updatedMedicineDTO);
            // Update existingMedicine with the fields of updatedMedicine
            existingMedicine.setName(updatedMedicine.getName());
            existingMedicine.setManufacturerName(updatedMedicine.getManufacturerName());
            existingMedicine.setGenericName(updatedMedicine.getGenericName());
            existingMedicine.setSellUom(updatedMedicine.getSellUom());
            existingMedicine.setUomPrice(updatedMedicine.getUomPrice());
            existingMedicine.setPackageSize(updatedMedicine.getPackageSize());
            existingMedicine.setUniqueIdentifier(updatedMedicine.getUniqueIdentifier());
            existingMedicine.setSoftDelete(updatedMedicine.isSoftDelete());
            return medicineMapper.toDTO(medicineRepository.save(existingMedicine));
        } else {
            return null;
        }
    }

    @Override
    public void deleteMedicine(Long id) {
        log.info("Deleting medicine with ID: {}", id);
        medicineRepository.deleteById(id);
    }
}

