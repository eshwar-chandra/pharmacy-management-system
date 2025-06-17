package com.pharmacy.management.customer; // Updated package

import com.pharmacy.management.customer.CustomerDTO; // Updated import
import com.pharmacy.management.customer.Customer; // Updated import
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    @Mapping(target = "id", source = "customerId")
    CustomerDTO toDTO(Customer customer);

    @Mapping(target = "customerId", source = "id")
    Customer toEntity(CustomerDTO customerDTO);
}
