package com.pharmacy.management.mapper;

import com.pharmacy.management.dto.StockReturnDTO;
import com.pharmacy.management.models.Stock;
import com.pharmacy.management.models.StockReturn;
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
