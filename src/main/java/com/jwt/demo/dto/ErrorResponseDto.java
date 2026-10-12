package com.jwt.demo.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class ErrorResponseDto {

    // Automatically sets the time the error occurred
    private final LocalDateTime timestamp = LocalDateTime.now();
    private final int status;
    private final String message;
    // Holds the specific @Valid failures (will be null for generic errors)
    private final Map<String, String> fieldErrors;

    public ErrorResponseDto(int status, String message, Map<String, String> fieldErrors) {
        this.status = status;
        this.message = message;
        this.fieldErrors = fieldErrors;
    }
}
