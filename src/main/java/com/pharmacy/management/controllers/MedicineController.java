package com.pharmacy.management.controllers;

import com.pharmacy.management.dto.MedicineDTO;
import com.pharmacy.management.services.MedicineService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/medicines")
public class MedicineController {

    private final MedicineService medicineService;

    @Autowired
    public MedicineController(MedicineService medicineService) {
        this.medicineService = medicineService;
    }

    @GetMapping("/{id}")
    public MedicineDTO getMedicineById(@PathVariable Long id) {
        log.info("Fetching medicine with ID: {}", id);
        return medicineService.getMedicineById(id);
    }

    @GetMapping
    public List<MedicineDTO> getAllMedicines() {
        log.info("Request to fetch all medicines");
        return medicineService.getAllMedicines();
    }

    @PostMapping
    public MedicineDTO addMedicine(@RequestBody MedicineDTO medicineDTO) {
        log.info("Adding new medicine: {}", medicineDTO);
        return medicineService.addMedicine(medicineDTO);
    }

    @PutMapping("/{id}")
    public MedicineDTO updateMedicine(@PathVariable Long id, @RequestBody MedicineDTO medicineDTO) {
        log.info("Updating medicine with ID {}: {}", id, medicineDTO);
        return medicineService.updateMedicine(id, medicineDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteMedicine(@PathVariable Long id) {
        log.info("Deleting medicine with ID: {}", id);
        medicineService.deleteMedicine(id);
    }
}


