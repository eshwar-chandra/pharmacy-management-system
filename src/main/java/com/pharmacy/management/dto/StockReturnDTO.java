package com.pharmacy.management.dto;

import lombok.Data;
import java.sql.Timestamp;

@Data
public class StockReturnDTO {
    private Long id; // return_id
    private Long stockId;
    private String medicineName; // For display, from Stock -> Medicine
    private Integer quantityReturned; // Quantity of the specific stock item returned
    private String reason;
    private Timestamp returnDate;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
