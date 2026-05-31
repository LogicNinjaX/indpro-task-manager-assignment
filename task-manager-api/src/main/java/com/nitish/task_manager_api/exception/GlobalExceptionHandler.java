package com.nitish.task_manager_api.exception;

import com.nitish.task_manager_api.model.dto.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> validationExceptionHandler(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> errors = new LinkedHashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(HttpStatus.BAD_REQUEST, "Validation failed", errors, request.getRequestURI()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> duplicateFieldHandler(DataIntegrityViolationException ex, HttpServletRequest request) {

        Throwable cause = ex.getRootCause();
        String responseMessage = "Duplicate value";

        if (cause != null) {
            String message = cause.getMessage();

            if (message.contains("uk_username")) {
                responseMessage = "Username already exists";
            }
            else if (message.contains("uk_email")) {
                responseMessage = "User email already exists";
            } else if (message.contains("uk_task_title")) {
                responseMessage = "Task already exist with this name";
            } else {
                responseMessage = message;
            }
        }

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(
                        HttpStatus.CONFLICT,
                        responseMessage,
                        null,
                        request.getRequestURI()
                ));
    }



    @ExceptionHandler({TaskNotFoundException.class, UserNotFoundException.class})
    public ResponseEntity<ErrorResponse> entityNotFoundHandler(RuntimeException e, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(HttpStatus.NOT_FOUND, e.getMessage(), null, request.getRequestURI()));
    }

    @ExceptionHandler({IllegalArgumentException.class,})
    public ResponseEntity<ErrorResponse> illegalArgumentHandler(RuntimeException e, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(HttpStatus.CONFLICT, e.getMessage(), null, request.getRequestURI()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception e, HttpServletRequest request) {

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                                "Something went wrong: " + e.getMessage(),
                                null,
                                request.getRequestURI()
                        )
                );
    }
}
