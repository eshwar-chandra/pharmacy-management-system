package com.pharmacy.management.stock; // Updated package

import com.pharmacy.management.stock.StockDTO; // Updated import
import com.pharmacy.management.medicine.Medicine; // Updated import - Assuming Medicine is in its new package
import com.pharmacy.management.stock.Stock; // Updated import
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface StockMapper {
    @Mappings({
        @Mapping(target = "id", source = "stockId"),
        @Mapping(target = "medicineId", source = "medicine.itemId"),
        @Mapping(target = "medicineName", source = "medicine.name")
    })
    StockDTO toDTO(Stock stock);

    @Mappings({
        @Mapping(target = "stockId", source = "id"),
        // The 'medicine' field in Stock entity is complex and usually set in the service layer
        // after fetching the Medicine entity. So, direct mapping from medicineId might be omitted
        // or handled via a custom method if only a shell is needed.
        // For now, we assume the service handles setting the Medicine object.
        @Mapping(target = "medicine", ignore = true)
    })
    Stock toEntity(StockDTO stockDTO);

    // This default method was in the original but is problematic for MapStruct
    // if Medicine itself is not simple or if we want MapStruct to resolve it.
    // The service layer is a better place to fetch and set the Medicine entity.
    // If kept, ensure Medicine class is imported and accessible.
    /*
    default Medicine longToMedicine(Long medicineId) {
       if (medicineId == null) {
           return null;
       }
       Medicine medicine = new Medicine();
       medicine.setItemId(medicineId);
       return medicine;
   }
   */
}
