package com.fitcare.exception;

import com.fitcare.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access
        .AccessDeniedException;
import org.springframework.security.authentication
        .BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind
        .MethodArgumentNotValidException;
import org.springframework.web.bind.annotation
        .ExceptionHandler;
import org.springframework.web.bind.annotation
        .RestControllerAdvice;
import org.springframework.web.method.annotation
        .MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ── Validation Errors (@Valid) ────────────
    @ExceptionHandler(
            MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<
    Map<String, String>>>
    handleValidation(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors =
                new HashMap<>();

        ex.getBindingResult()
                .getAllErrors()
                .forEach(e -> {
                    String field =
                            ((FieldError) e)
                                    .getField();
                    String msg =
                            e.getDefaultMessage();
                    errors.put(field, msg);
                });

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse
                        .<Map<String, String>>builder()
                        .success(false)
                        .message("Validation failed."
                                + " Check your input.")
                        .data(errors)
                        .timestamp(
                                LocalDateTime.now())
                        .build());
    }

    // ── Bad Credentials ───────────────────────
    @ExceptionHandler(
            BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Void>>
    handleBadCredentials(
            BadCredentialsException ex) {

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse
                        .<Void>builder()
                        .success(false)
                        .message(
                                "Invalid email "
                                        + "or password.")
                        .timestamp(
                                LocalDateTime.now())
                        .build());
    }

    // ── Access Denied (403) ───────────────────
    @ExceptionHandler(
            AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>>
    handleAccessDenied(
            AccessDeniedException ex) {

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ApiResponse
                        .<Void>builder()
                        .success(false)
                        .message(
                                "Access denied. "
                                        + "You don't have "
                                        + "permission.")
                        .timestamp(
                                LocalDateTime.now())
                        .build());
    }

    // ── Invalid Enum / Argument ───────────────
    @ExceptionHandler(
            IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>>
    handleIllegalArgument(
            IllegalArgumentException ex) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse
                        .<Void>builder()
                        .success(false)
                        .message(ex.getMessage())
                        .timestamp(
                                LocalDateTime.now())
                        .build());
    }

    // ── Wrong URL Parameter Type ──────────────
    @ExceptionHandler(
            MethodArgumentTypeMismatchException
                    .class)
    public ResponseEntity<ApiResponse<Void>>
    handleTypeMismatch(
            MethodArgumentTypeMismatchException
                    ex) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse
                        .<Void>builder()
                        .success(false)
                        .message(
                                "Invalid parameter: '"
                                        + ex.getName()
                                        + "' should be "
                                        + ex.getRequiredType()
                                        .getSimpleName())
                        .timestamp(
                                LocalDateTime.now())
                        .build());
    }

    // ── Runtime Exception ─────────────────────
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Void>>
    handleRuntime(
            RuntimeException ex) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse
                        .<Void>builder()
                        .success(false)
                        .message(ex.getMessage())
                        .timestamp(
                                LocalDateTime.now())
                        .build());
    }

    // ── Generic Exception ─────────────────────
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>>
    handleGeneric(Exception ex) {

        return ResponseEntity
                .status(
                        HttpStatus
                                .INTERNAL_SERVER_ERROR)
                .body(ApiResponse
                        .<Void>builder()
                        .success(false)
                        .message(
                                "Something went "
                                        + "wrong. Please "
                                        + "try again.")
                        .timestamp(
                                LocalDateTime.now())
                        .build());
    }
}