package com.pharmacy.management.sale.strategy;

// Ensure this import points to the new location of Sale model
import com.pharmacy.management.sale.Sale;

public interface PostSaleActionStrategy {
    void execute(Sale sale);
}
