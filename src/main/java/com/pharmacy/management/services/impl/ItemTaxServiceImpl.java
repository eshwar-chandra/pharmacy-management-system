package com.pharmacy.management.services.impl;

import com.pharmacy.management.dtos.request.TaxRequestDto;
import com.pharmacy.management.models.Item;
import com.pharmacy.management.models.ItemTax;
import com.pharmacy.management.models.Tax;
import com.pharmacy.management.repositories.ItemTaxRepository;
import com.pharmacy.management.repositories.TaxRepository;
import com.pharmacy.management.services.ItemTaxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemTaxServiceImpl implements ItemTaxService {

    @Autowired
    private ItemTaxRepository itemTaxRepository;

    @Autowired
    private TaxRepository taxRepository;

@Override
public void saveAllItemMapping(List<TaxRequestDto> taxes, Item item) {
    List<ItemTax> itemTaxes = new ArrayList<>();

    for (TaxRequestDto taxRequestDto : taxes) {
        Tax tax = taxRepository.findById(taxRequestDto.getId())
                .orElseThrow(() -> new RuntimeException("Tax not found"));
        ItemTax itemTax = new ItemTax();
        itemTax.setItem(item);
        itemTax.setTax(tax);
        itemTaxes.add(itemTax);
    }

    itemTaxRepository.saveAll(itemTaxes);
}

}