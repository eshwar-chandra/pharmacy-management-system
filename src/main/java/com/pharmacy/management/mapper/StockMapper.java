package com.pharmacy.management.mapper;

import com.pharmacy.management.dto.StockDTO;
import com.pharmacy.management.models.Stock;
import com.pharmacy.management.models.Medicine; // Required for mapping if Medicine object is constructed
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface StockMapper {
    @Mappings({
        @Mapping(target = "id", source = "stockId"),
        @Mapping(target = "medicineId", source = "medicine.itemId"),
        @Mapping(target = "medicineName", source = "medicine.name") // Populate medicineName
    })
    StockDTO toDTO(Stock stock);

    @Mappings({
        @Mapping(target = "stockId", source = "id"),
        @Mapping(target = "medicine", source = "medicineId") // Handled in service layer
    })
    Stock toEntity(StockDTO stockDTO);

    // Helper to map Long to Medicine object shell for setting in entity
    // This is a simplified approach. Usually, the Medicine entity would be fetched in the service.
    default Medicine longToMedicine(Long medicineId) {
       if (medicineId == null) {
           return null;
       }
       Medicine medicine = new Medicine();
       medicine.setItemId(medicineId);
       return medicine;
   }
}
