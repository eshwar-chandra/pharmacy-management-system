package com.pharmacy.management.sale; // Updated package

import com.pharmacy.management.medicine.Medicine; // Updated import
import com.pharmacy.management.sale.SalesItemDTO; // Updated import
import com.pharmacy.management.sale.SalesItem; // Updated import
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
    SalesItem toEntity(SalesItemDTO salesItemDTO);

    @Named("longToMedicineShell")
    default Medicine longToMedicineShell(Long medicineId) {
        if (medicineId == null) return null;
        Medicine medicine = new Medicine();
        medicine.setItemId(medicineId);
        return medicine;
    }
}
