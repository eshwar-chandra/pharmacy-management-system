package com.pharmacy.management.sale.strategy;

import com.pharmacy.management.sale.Sale; // Correct import
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component // Register as a Spring bean to be collected in a list
public class EmailNotificationStrategy implements PostSaleActionStrategy {
    @Override
    public void execute(Sale sale) {
        // In a real application, this would integrate with an email service
        log.info("Executing Email Notification Strategy for Sale ID: {}. Customer Email: {}",
                 sale.getSaleId(),
                 sale.getCustomer() != null ? sale.getCustomer().getEmail() : "N/A");
        // Simulate sending email
    }
}
