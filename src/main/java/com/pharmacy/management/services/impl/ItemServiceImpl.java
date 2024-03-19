package com.pharmacy.management.services.impl;

import com.pharmacy.management.dtos.request.ItemRequestDto;
import com.pharmacy.management.dtos.request.TaxRequestDto;
import com.pharmacy.management.dtos.response.ItemResponseDto;
import com.pharmacy.management.models.Item;
import com.pharmacy.management.mapper.ItemMapper;
import com.pharmacy.management.repositories.ItemRepository;
import com.pharmacy.management.services.ItemService;
import com.pharmacy.management.services.ItemTaxService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final ItemTaxService itemTaxService;

    public ItemServiceImpl(ItemRepository itemRepository, ItemMapper itemMapper, ItemTaxService itemTaxService) {
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
        this.itemTaxService = itemTaxService;
    }
@Override
public ItemResponseDto createItem(ItemRequestDto itemRequestDto) {
    Item item = itemMapper.itemRequestDtoToItem(itemRequestDto);
    Item savedItem = itemRepository.save(item);
    // Save all item taxes in a batch
    itemTaxService.saveAllItemMapping(itemRequestDto.getTaxes(), savedItem);
    return itemMapper.itemToItemResponseDto(savedItem);
}

    @Override
    public ItemResponseDto updateItem(Long id, ItemRequestDto itemRequestDto) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new RuntimeException("Item not found"));
        Item updatedItemRequest = itemMapper.itemRequestDtoToItem(itemRequestDto);
        updatedItemRequest.setId(item.getId()); // Ensure we're updating the correct item
        Item updatedItem = itemRepository.save(updatedItemRequest);
        return itemMapper.itemToItemResponseDto(updatedItem);
    }

    @Override
    public ItemResponseDto getItem(Long id) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new RuntimeException("Item not found"));
        return itemMapper.itemToItemResponseDto(item);
    }

    @Override
    public List<ItemResponseDto> getAllItems() {
        List<Item> items = itemRepository.findAll();
        return items.stream()
                .map(itemMapper::itemToItemResponseDto)
                .collect(Collectors.toList());
    }
}