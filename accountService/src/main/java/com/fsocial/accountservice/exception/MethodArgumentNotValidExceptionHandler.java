package com.fsocial.accountservice.exception;

import com.fsocial.accountservice.dto.ApiResponse;
import com.fsocial.accountservice.enums.ValidErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.Objects;

@ControllerAdvice
public class MethodArgumentNotValidExceptionHandler {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handleValidException(MethodArgumentNotValidException exception) {
        String enumKey = Objects.requireNonNull(exception.getFieldError()).getDefaultMessage();
        ValidErrorCode errorCode = ValidErrorCode.valueOf(enumKey);
        return ResponseEntity.badRequest().body(ApiResponse.builder()
                .statusCode(errorCode.getCode())
                .message(errorCode.getMessage())
                .dateTime(LocalDateTime.now())
                .build());
    }
}
