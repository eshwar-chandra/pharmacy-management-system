package com.pharmacy.management.mapper;

import com.pharmacy.management.dto.SalesItemDTO;
import com.pharmacy.management.models.Medicine;
import com.pharmacy.management.models.SalesItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface SalesItemMapper {
    @Mapping(target = "id", source = "salesItemId")
    @Mapping(target = "medicineId", source = "medicine.itemId")
    @Mapping(target = "medicineName", source = "medicine.name")
    SalesItemDTO toDTO(SalesItem salesItem);

    @Mapping(target = "salesItemId", source = "id")
    @Mapping(target = "medicine", source = "medicineId", qualifiedByName = "longToMedicineShell")
    // sale is set in SaleService
    SalesItem toEntity(SalesItemDTO salesItemDTO);

    @Named("longToMedicineShell")
    default Medicine longToMedicineShell(Long medicineId) {
        if (medicineId == null) return null;
        Medicine medicine = new Medicine();
        medicine.setItemId(medicineId);
        // Other essential fields for a "shell" if needed by persistence context
        return medicine;
    }
}
