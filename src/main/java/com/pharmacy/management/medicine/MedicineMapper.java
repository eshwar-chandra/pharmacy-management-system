package com.pharmacy.management.medicine; // Updated package

import com.pharmacy.management.medicine.MedicineDTO; // Updated import
import com.pharmacy.management.medicine.Medicine; // Updated import
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MedicineMapper {

    @Mapping(target = "id", source = "itemId")
    MedicineDTO toDTO(Medicine medicine);

    @Mapping(target = "itemId", source = "id")
    Medicine toEntity(MedicineDTO medicineDTO);
}
