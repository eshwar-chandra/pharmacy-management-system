package com.pharmacy.management.services;

import com.pharmacy.management.dtos.request.TaxRequestDto;
import com.pharmacy.management.models.Item;

import java.util.List;

public interface ItemTaxService {
    void saveAllItemMapping(List<TaxRequestDto> taxes, Item item);
}