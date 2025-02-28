package com.fsocial.profileservice.exception;

import com.fsocial.profileservice.dto.ApiResponse;
import com.fsocial.profileservice.enums.ErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse> handlingRuntimeException(RuntimeException exception) {
        return buildResponse(ErrorCode.UNCATEGORIZED_EXCEPTION);
    }

    @ExceptionHandler(value = AppCheckedException.class)
    ResponseEntity<ApiResponse> handlingAppCheckedException(AppCheckedException exception) {
        return ResponseEntity
                .badRequest()
                .body(ApiResponse.builder()
                        .statusCode(exception.getStatus().getCode())
                        .message(exception.getStatus().getMessage())
                        .dateTime(LocalDateTime.now())
                        .build());
    }

    @ExceptionHandler(value = NoResourceFoundException.class)
    ResponseEntity<ApiResponse> handlingNotFoundException(NoResourceFoundException exception) {
        ErrorCode errorCode = ErrorCode.NOT_FOUND;
        return ResponseEntity.badRequest().body(ApiResponse.builder()
                .statusCode(errorCode.getCode())
                .message(errorCode.getMessage())
                .dateTime(LocalDateTime.now())
                .build());
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiResponse> handleAccessDeniedException(AccessDeniedException exception) {
        return buildResponse(ErrorCode.UNAUTHENTICATED);
    }

    private ResponseEntity<ApiResponse> buildResponse(ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getHttpStatusCode())
                .body(ApiResponse.builder()
                        .statusCode(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .dateTime(LocalDateTime.now())
                        .build());
    }
}