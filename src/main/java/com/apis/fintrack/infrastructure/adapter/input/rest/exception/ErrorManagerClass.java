package com.apis.fintrack.infrastructure.adapter.input.rest.exception;


import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO used to represent error details returned by the API.
 * It standardizes error responses, including status code, message,
 * timestamp, and optionally a list of validation errors.
 */
@Getter
@Setter
@NoArgsConstructor
@Schema(
        name = "ErrorManagerClass",
        description = "Represents a structured error response returned by the API, containing details about the failure."
)
public class ErrorManagerClass {

    @Schema(
            description = "HTTP status code of the error response.",
            example = "404",
            required = true
    )
    private int status;

    @Schema(
            description = "Human-readable description of the error or exception cause.",
            example = "User not found.",
            required = true
    )
    private String message;

    @Schema(
            description = "Timestamp when the error occurred, in ISO-8601 format.",
            example = "2025-11-01T15:30:45",
            required = true
    )
    private LocalDateTime timestamp;

    @ArraySchema(
            schema = @Schema(description = "List of specific validation or constraint error messages."),
            arraySchema = @Schema(description = "Collection of detailed error messages.")
    )
    private List<String> errors;

    public ErrorManagerClass(int status, String message, LocalDateTime timestamp) {
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
    }

    public ErrorManagerClass(int status, String message, LocalDateTime timestamp, List<String> errors) {
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
        this.errors = errors;
    }
}

