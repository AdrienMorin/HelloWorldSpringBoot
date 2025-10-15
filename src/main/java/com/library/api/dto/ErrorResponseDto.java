package com.library.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Transfer Object for error responses.
 *
 * Provides a consistent error response structure across the API,
 * including timestamp, status, error details, and validation errors.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Error response containing details about the error")
public class ErrorResponseDto {

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Timestamp when the error occurred")
    private LocalDateTime timestamp;

    @Schema(description = "HTTP status code", example = "400")
    private Integer status;

    @Schema(description = "Error type", example = "Bad Request")
    private String error;

    @Schema(description = "Detailed error message", example = "Invalid request parameters")
    private String message;

    @Schema(description = "API path where the error occurred", example = "/api/v1/books")
    private String path;

    @Schema(description = "List of validation errors")
    private List<ValidationError> validationErrors;

    /**
     * Nested class representing individual validation errors.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "Individual validation error details")
    public static class ValidationError {

        @Schema(description = "Field name that failed validation", example = "isbn")
        private String field;

        @Schema(description = "Rejected value", example = "invalid-isbn")
        private Object rejectedValue;

        @Schema(description = "Validation error message", example = "Invalid ISBN format")
        private String message;
    }
}
