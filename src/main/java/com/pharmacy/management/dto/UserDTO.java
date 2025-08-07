package com.pharmacy.management.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    @Schema(description = "User ID", example = "101", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Username", example = "john.doe")
    @NotBlank(message = "Username cannot be blank")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    @Schema(description = "User's email address", example = "john.doe@example.com")
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be valid")
    private String email;

    @Schema(description = "User's password (only for create/update, not returned in GET responses)", example = "securePwd123", accessMode = Schema.AccessMode.WRITE_ONLY)
    @NotBlank(message = "Password is required for creating/updating user")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @Schema(description = "Timestamp of user creation", accessMode = Schema.AccessMode.READ_ONLY)
    private Timestamp createdAt;

    @Schema(description = "Timestamp of last user update", accessMode = Schema.AccessMode.READ_ONLY)
    private Timestamp updatedAt;
}
