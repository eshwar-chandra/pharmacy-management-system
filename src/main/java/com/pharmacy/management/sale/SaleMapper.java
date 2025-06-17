package com.pharmacy.management.sale; // Updated package

import com.pharmacy.management.customer.Customer; // Updated import
import com.pharmacy.management.sale.SaleDTO; // Updated import
import com.pharmacy.management.sale.Sale; // Updated import
import com.pharmacy.management.sale.SalesItemMapper; // Updated import
import com.pharmacy.management.user.User; // Updated import
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = {SalesItemMapper.class})
public interface SaleMapper {
    @Mapping(target = "id", source = "saleId")
    @Mapping(target = "userId", source = "user.userId")
    @Mapping(target = "userName", source = "user.username")
    @Mapping(target = "customerId", source = "customer.customerId")
    @Mapping(target = "customerName", source = "customer.name")
    @Mapping(target = "items", source = "salesItems")
    SaleDTO toDTO(Sale sale);

    @Mapping(target = "saleId", source = "id")
    @Mapping(target = "user", source = "userId", qualifiedByName = "longToUserShell")
    @Mapping(target = "customer", source = "customerId", qualifiedByName = "longToCustomerShell")
    @Mapping(target = "salesItems", source = "items")
    Sale toEntity(SaleDTO saleDTO);

    @Named("longToUserShell")
    default User longToUserShell(Long userId) {
        if (userId == null) return null;
        User user = new User();
        user.setUserId(userId);
        return user;
    }

    @Named("longToCustomerShell")
    default Customer longToCustomerShell(Long customerId) {
        if (customerId == null) return null;
        Customer customer = new Customer();
        customer.setCustomerId(customerId);
        return customer;
    }
}
