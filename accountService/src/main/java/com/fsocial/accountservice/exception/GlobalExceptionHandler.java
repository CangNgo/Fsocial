package com.fsocial.accountservice.exception;

import com.fsocial.accountservice.dto.ApiResponse;
import com.fsocial.accountservice.enums.ErrorCode;
import com.fsocial.accountservice.enums.ValidCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.Objects;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse> handlingRuntimeException(RuntimeException exception) {
        log.error("Lỗi ở RuntimeException chưa được xử lý: {}", exception.getMessage());
        return ResponseEntity.internalServerError().body(ApiResponse.builder()
                .statusCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode())
                .message(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage())
                .dateTime(LocalDateTime.now())
                .data(null)
                .build());
    }

    @ExceptionHandler(value = AppCheckedException.class)
    ResponseEntity<ApiResponse> handlingAppCheckedException(AppCheckedException exception) {
        return ResponseEntity
                .badRequest()
                .body(createApiResponse(exception.getStatus()));
    }

    @ExceptionHandler(value = NoResourceFoundException.class)
    ResponseEntity<ApiResponse> handlingNotFoundException(NoResourceFoundException exception) {
        return ResponseEntity
                .badRequest()
                .body(ApiResponse.builder()
                    .statusCode(ErrorCode.NOT_FOUND.getCode())
                    .message(ErrorCode.NOT_FOUND.getMessage())
                    .dateTime(LocalDateTime.now())
                    .build()
                );
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handlingMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        String enumKey = Objects.requireNonNull(exception.getFieldError()).getDefaultMessage();
        ValidCode validCode = ValidCode.valueOf(enumKey);
        return ResponseEntity
                .badRequest()
                .body(createApiResponse(validCode));
    }

    @ExceptionHandler(value = AppException.class)
     ResponseEntity<ApiResponse> handleAppException(AppException exception) {
        ErrorCode errorCode = exception.getStatusCode();
        return ResponseEntity
                .status(errorCode.getStatusCode())
                .body(createApiResponse(errorCode));
    }

    private ApiResponse createApiResponse(ErrorCode statusCode) {
        return ApiResponse.builder()
                .statusCode(statusCode.getCode())
                .message(statusCode.getMessage())
                .dateTime(LocalDateTime.now())
                .build();
    }

    private ApiResponse createApiResponse(ValidCode validCode) {
        return ApiResponse.builder()
                .statusCode(validCode.getCode())
                .message(validCode.getMessage())
                .dateTime(LocalDateTime.now())
                .build();
    }
}
