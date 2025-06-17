package com.pharmacy.management.stockreturn; // Updated package

import com.pharmacy.management.stockreturn.StockReturnDTO; // Updated import
import com.pharmacy.management.stock.Stock; // Updated import
import com.pharmacy.management.stockreturn.StockReturn; // Updated import
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface StockReturnMapper {
    @Mapping(target = "id", source = "returnId")
    @Mapping(target = "stockId", source = "stock.stockId")
    @Mapping(target = "medicineName", source = "stock.medicine.name")
    StockReturnDTO toDTO(StockReturn stockReturn);

    @Mapping(target = "returnId", source = "id")
    @Mapping(target = "stock", source = "stockId", qualifiedByName = "longToStockShell")
    StockReturn toEntity(StockReturnDTO stockReturnDTO);

    @Named("longToStockShell")
    default Stock longToStockShell(Long stockId) {
        if (stockId == null) return null;
        Stock stock = new Stock();
        stock.setStockId(stockId);
        return stock;
    }
}
