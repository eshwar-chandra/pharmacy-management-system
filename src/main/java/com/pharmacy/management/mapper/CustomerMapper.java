package com.pharmacy.management.mapper;

import com.pharmacy.management.dto.CustomerDTO;
import com.pharmacy.management.models.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    @Mapping(target = "id", source = "customerId")
    CustomerDTO toDTO(Customer customer);

    @Mapping(target = "customerId", source = "id")
    Customer toEntity(CustomerDTO customerDTO);
}
