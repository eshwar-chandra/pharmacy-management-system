package com.pharmacy.management.mapper;

import org.mapstruct.Mapper;
import com.pharmacy.management.dtos.request.ItemRequestDto;
import com.pharmacy.management.dtos.response.ItemResponseDto;
import com.pharmacy.management.models.Item;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    ItemResponseDto itemToItemResponseDto(Item item);
    Item itemRequestDtoToItem(ItemRequestDto itemRequestDto);
}