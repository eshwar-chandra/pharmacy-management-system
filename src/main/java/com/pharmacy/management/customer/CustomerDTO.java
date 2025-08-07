package com.pharmacy.management.customer; // Updated package

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDTO {
    @Schema(description = "Customer ID", example = "201", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Full name of the customer", example = "Jane Smith")
    @NotBlank(message = "Customer name cannot be blank")
    private String name;

    @Schema(description = "Customer's phone number", example = "+11234567890")
    @Pattern(regexp = "^$|^\\+?[0-9. ()-]{7,25}$", message = "Phone number is not valid") // Allows empty or valid format
    private String phoneNumber; // Optional

    @Schema(description = "Customer's email address", example = "jane.smith@example.com")
    @Email(message = "Email should be valid if provided")
    private String email; // Optional, but if present, must be valid

    @Schema(description = "Timestamp of customer record creation", accessMode = Schema.AccessMode.READ_ONLY)
    private Timestamp createdAt;

    @Schema(description = "Timestamp of last customer record update", accessMode = Schema.AccessMode.READ_ONLY)
    private Timestamp updatedAt;
}
