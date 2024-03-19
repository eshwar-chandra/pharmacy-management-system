package com.pharmacy.management.services;

import com.pharmacy.management.dtos.request.ItemRequestDto;
import com.pharmacy.management.dtos.response.ItemResponseDto;
import java.util.List;

public interface ItemService {
    ItemResponseDto createItem(ItemRequestDto itemRequestDto);
    ItemResponseDto updateItem(Long id, ItemRequestDto itemRequestDto);
    ItemResponseDto getItem(Long id);
    List<ItemResponseDto> getAllItems();
}