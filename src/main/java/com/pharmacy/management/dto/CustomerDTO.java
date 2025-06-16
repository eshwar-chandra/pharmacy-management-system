package com.pharmacy.management.dto;

import lombok.Data;
import java.sql.Timestamp;

@Data
public class CustomerDTO {
    private Long id;
    private String name;
    private String phoneNumber;
    private String email;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
