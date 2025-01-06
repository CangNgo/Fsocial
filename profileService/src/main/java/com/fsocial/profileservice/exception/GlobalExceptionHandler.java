package com.fsocial.profileservice.exception;

import com.fsocial.profileservice.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse> handlingRuntimeException(RuntimeException exception) {

        return ResponseEntity.badRequest().body(ApiResponse.builder()
                .statusCode(StatusCode.UNCATEGORIZED_EXCEPTION.getCode())
                .message(StatusCode.UNCATEGORIZED_EXCEPTION.getMessage())
                .dateTime(LocalDateTime.now())
                .data(null)
                .build());
    }

    @ExceptionHandler(value = AppCheckedException.class)
    ResponseEntity<ApiResponse> handlingAppCheckedException(AppCheckedException exception) {
        StatusCode statusCode = exception.getStatus();
        return ResponseEntity.badRequest().body(ApiResponse.builder()
                .statusCode(statusCode.getCode())
                .message(exception.getMessage())
                .build());
    }

    @ExceptionHandler(value = NoResourceFoundException.class)
    ResponseEntity<ApiResponse> handlingNotFoundException(NoResourceFoundException exception) {
        return ResponseEntity.badRequest().body(ApiResponse.builder()
                .statusCode(StatusCode.UNCATEGORIZED_EXCEPTION.getCode())
                .message(StatusCode.UNCATEGORIZED_EXCEPTION.getMessage())
                .build());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handlingMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        return ResponseEntity.badRequest().body(ApiResponse.builder()
                .statusCode(exception.getStatusCode().value())
                .message(Objects.requireNonNull(exception.getFieldError()).getDefaultMessage())
                .build());
    }
}
