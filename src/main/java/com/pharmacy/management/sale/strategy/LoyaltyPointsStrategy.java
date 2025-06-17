package com.pharmacy.management.sale.strategy;

import com.pharmacy.management.sale.Sale; // Correct import
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Slf4j
@Component // Register as a Spring bean
public class LoyaltyPointsStrategy implements PostSaleActionStrategy {
    private static final BigDecimal POINTS_RATE = new BigDecimal("0.10");

    @Override
    public void execute(Sale sale) {
        if (sale.getCustomer() != null && sale.getTotalAmount() != null) {
            // In a real app, fetch customer's loyalty account and add points
            BigDecimal pointsEarned = sale.getTotalAmount().multiply(POINTS_RATE);
            log.info("Executing Loyalty Points Strategy for Sale ID: {}. Customer ID: {}. Points Earned: {}",
                     sale.getSaleId(), sale.getCustomer().getCustomerId(), pointsEarned.intValue());
        } else {
            log.info("Skipping Loyalty Points Strategy for Sale ID: {} as customer or total amount is null.", sale.getSaleId());
        }
    }
}
