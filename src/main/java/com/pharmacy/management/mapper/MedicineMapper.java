package com.pharmacy.management.mapper;

import com.pharmacy.management.dto.MedicineDTO;
import com.pharmacy.management.models.Medicine;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MedicineMapper {

    @Mapping(target = "id", source = "itemId")
    MedicineDTO toDTO(Medicine medicine);

    @Mapping(target = "itemId", source = "id")
    Medicine toEntity(MedicineDTO medicineDTO);
}

