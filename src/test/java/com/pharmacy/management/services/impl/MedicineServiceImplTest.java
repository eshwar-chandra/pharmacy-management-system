package com.pharmacy.management.services.impl;

import com.pharmacy.management.dto.MedicineDTO;
import com.pharmacy.management.mapper.MedicineMapper;
import com.pharmacy.management.models.Medicine;
import com.pharmacy.management.repositories.MedicineRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedicineServiceImplTest {

    @Mock
    private MedicineRepository medicineRepository;

    @Mock
    private MedicineMapper medicineMapper;

    @InjectMocks
    private MedicineServiceImpl medicineService;

    private Medicine medicine;
    private MedicineDTO medicineDTO;

    @BeforeEach
    void setUp() {
        medicine = new Medicine();
        medicine.setItemId(1L);
        medicine.setName("TestMed");
        medicine.setSoftDelete(false);
        medicine.setUomPrice(BigDecimal.TEN);

        medicineDTO = new MedicineDTO();
        medicineDTO.setId(1L);
        medicineDTO.setName("TestMedDTO");
        medicineDTO.setUomPrice(BigDecimal.TEN);
    }

    @Test
    void getMedicineById_found() {
        when(medicineRepository.findById(1L)).thenReturn(Optional.of(medicine));
        when(medicineMapper.toDTO(medicine)).thenReturn(medicineDTO);

        MedicineDTO result = medicineService.getMedicineById(1L);

        assertNotNull(result);
        assertEquals("TestMedDTO", result.getName());
        verify(medicineRepository).findById(1L);
        verify(medicineMapper).toDTO(medicine);
    }

    @Test
    void getMedicineById_notFound() {
        when(medicineRepository.findById(1L)).thenReturn(Optional.empty());

        MedicineDTO result = medicineService.getMedicineById(1L);

        assertNull(result);
        verify(medicineRepository).findById(1L);
        verify(medicineMapper, never()).toDTO(any());
    }

    @Test
    void getAllMedicines_success() {
        when(medicineRepository.findAll()).thenReturn(Collections.singletonList(medicine));
        when(medicineMapper.toDTO(medicine)).thenReturn(medicineDTO);

        List<MedicineDTO> results = medicineService.getAllMedicines();

        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("TestMedDTO", results.get(0).getName());
        verify(medicineRepository).findAll();
        verify(medicineMapper).toDTO(medicine);
    }

    @Test
    void getAllMedicines_empty() {
        when(medicineRepository.findAll()).thenReturn(Collections.emptyList());

        List<MedicineDTO> results = medicineService.getAllMedicines();

        assertNotNull(results);
        assertTrue(results.isEmpty());
        verify(medicineRepository).findAll();
        verify(medicineMapper, never()).toDTO(any());
    }


    @Test
    void addMedicine_success() {
        when(medicineMapper.toEntity(medicineDTO)).thenReturn(medicine);
        when(medicineRepository.save(medicine)).thenReturn(medicine);
        when(medicineMapper.toDTO(medicine)).thenReturn(medicineDTO);

        MedicineDTO result = medicineService.addMedicine(medicineDTO);

        assertNotNull(result);
        assertEquals("TestMedDTO", result.getName());
        verify(medicineMapper).toEntity(medicineDTO);
        verify(medicineRepository).save(medicine);
        verify(medicineMapper).toDTO(medicine);
    }

    @Test
    void updateMedicine_found() {
        when(medicineRepository.findById(1L)).thenReturn(Optional.of(medicine));
        when(medicineMapper.toEntity(medicineDTO)).thenReturn(medicine); // Simplified for test
        when(medicineRepository.save(any(Medicine.class))).thenReturn(medicine);
        when(medicineMapper.toDTO(any(Medicine.class))).thenReturn(medicineDTO);

        MedicineDTO updatedDTO = new MedicineDTO(); // Assume this is the input DTO
        updatedDTO.setName("UpdatedName");


        MedicineDTO result = medicineService.updateMedicine(1L, updatedDTO);

        assertNotNull(result);
        // We need to mock the behavior of medicineMapper.toEntity(updatedDTO) for specific fields
        // For now, just checking if save was called and DTO is returned
        verify(medicineRepository).findById(1L);
        verify(medicineRepository).save(any(Medicine.class)); // Check that save was called
        verify(medicineMapper).toDTO(any(Medicine.class));
    }

    @Test
    void updateMedicine_notFound() {
        when(medicineRepository.findById(1L)).thenReturn(Optional.empty());
         MedicineDTO updatedDTO = new MedicineDTO();
        updatedDTO.setName("UpdatedName");

        MedicineDTO result = medicineService.updateMedicine(1L, updatedDTO);

        assertNull(result);
        verify(medicineRepository).findById(1L);
        verify(medicineRepository, never()).save(any());
    }

    @Test
    void deleteMedicine_softDeleteSuccess() {
        when(medicineRepository.findById(1L)).thenReturn(Optional.of(medicine));
        when(medicineRepository.save(any(Medicine.class))).thenReturn(medicine); // Mock save for soft delete

        medicineService.deleteMedicine(1L);

        assertTrue(medicine.isSoftDelete()); // Check flag is set
        verify(medicineRepository).findById(1L);
        verify(medicineRepository).save(medicine); // Verify save was called on the modified entity
    }

    @Test
    void deleteMedicine_notFound() {
        when(medicineRepository.findById(1L)).thenReturn(Optional.empty());

        medicineService.deleteMedicine(1L);

        verify(medicineRepository).findById(1L);
        verify(medicineRepository, never()).save(any());
    }
}
