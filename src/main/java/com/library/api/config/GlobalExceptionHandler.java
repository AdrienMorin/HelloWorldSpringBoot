package com.library.api.config;

import com.library.api.dto.ErrorResponseDto;
import com.library.api.exception.BookNotFoundException;
import com.library.api.exception.DuplicateIsbnException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Global exception handler for the application.
 *
 * This class intercepts exceptions thrown by controllers and service layers,
 * providing consistent error responses across the entire API.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Handles BookNotFoundException.
     *
     * @param ex the exception
     * @param request the HTTP request
     * @return error response with HTTP 404 status
     */
    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleBookNotFoundException(
            BookNotFoundException ex,
            HttpServletRequest request) {

        log.error("Book not found: {}", ex.getMessage());

        ErrorResponseDto errorResponse = ErrorResponseDto.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    /**
     * Handles DuplicateIsbnException.
     *
     * @param ex the exception
     * @param request the HTTP request
     * @return error response with HTTP 400 status
     */
    @ExceptionHandler(DuplicateIsbnException.class)
    public ResponseEntity<ErrorResponseDto> handleDuplicateIsbnException(
            DuplicateIsbnException ex,
            HttpServletRequest request) {

        log.error("Duplicate ISBN: {}", ex.getMessage());

        ErrorResponseDto errorResponse = ErrorResponseDto.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Handles validation errors from @Valid annotation.
     *
     * @param ex the exception
     * @param request the HTTP request
     * @return error response with HTTP 400 status and detailed validation errors
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        log.error("Validation error: {}", ex.getMessage());

        List<ErrorResponseDto.ValidationError> validationErrors = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> {
                    String fieldName = error instanceof FieldError
                            ? ((FieldError) error).getField()
                            : error.getObjectName();
                    Object rejectedValue = error instanceof FieldError
                            ? ((FieldError) error).getRejectedValue()
                            : null;
                    String message = error.getDefaultMessage();

                    return ErrorResponseDto.ValidationError.builder()
                            .field(fieldName)
                            .rejectedValue(rejectedValue)
                            .message(message)
                            .build();
                })
                .collect(Collectors.toList());

        ErrorResponseDto errorResponse = ErrorResponseDto.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message("Validation failed for one or more fields")
                .path(request.getRequestURI())
                .validationErrors(validationErrors)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Handles all other unexpected exceptions.
     *
     * @param ex the exception
     * @param request the HTTP request
     * @return error response with HTTP 500 status
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGlobalException(
            Exception ex,
            HttpServletRequest request) {

        log.error("Unexpected error occurred", ex);

        ErrorResponseDto errorResponse = ErrorResponseDto.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .message("An unexpected error occurred. Please try again later.")
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
