package com.pharmacy.management.common.exception; // Updated package

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Builder;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL) // Don't include null fields in JSON output
public class ErrorResponseDTO {
    private LocalDateTime timestamp;
    private int status;
    private String error; // e.g., "Not Found", "Bad Request"
    private String message;
    private String path;
    private List<ErrorDetail> details; // For validation errors
}
