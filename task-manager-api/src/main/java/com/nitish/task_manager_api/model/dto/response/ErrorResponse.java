package com.nitish.task_manager_api.model.dto.response;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

public record ErrorResponse(
        boolean success,
        int status,
        String message,
        Map<String, String> errors,
        String path,
        LocalDateTime timestamp
) {
    public ErrorResponse(HttpStatus status, String message, Map<String, String> errors, String path) {
        this(false, status.value(), message, errors, path, LocalDateTime.now());
    }
}