package com.pharmacy.management.dto;

import lombok.Data;
import java.sql.Timestamp;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    // Password is generally not included in DTOs returned to clients
    // For creation/update, a specific DTO might be used or password handled separately
    private String password; // For simplicity in this step, including for add/update path
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
