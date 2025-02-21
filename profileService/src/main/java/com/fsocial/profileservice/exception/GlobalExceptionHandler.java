package com.fsocial.profileservice.exception;

import com.fsocial.profileservice.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
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
                .message("Lỗi gì đó mà chúng tôi cũng không biết hihi :D" + exception.getMessage())
                .dateTime(LocalDateTime.now())
                .build());
    }

    @ExceptionHandler(value = AppCheckedException.class)
    ResponseEntity<ApiResponse> handlingAppCheckedException(AppCheckedException exception) {
        return ResponseEntity.badRequest().body(ApiResponse.builder()
                .statusCode(exception.getStatus().getCode())
                .message(exception.getMessage()).dateTime(LocalDateTime.now())
                .build());
    }

    @ExceptionHandler(value = NoResourceFoundException.class)
    ResponseEntity<ApiResponse> handlingNotFoundException(NoResourceFoundException exception) {
        return ResponseEntity.badRequest().body(ApiResponse.builder()
                .statusCode(StatusCode.ENPOINTMENT_NOT_FOUND.getCode())
                .message("Không tìm thấy enpoint: " + exception.getResourcePath())
                .dateTime(LocalDateTime.now())
                .build());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handlingMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        return ResponseEntity.badRequest().body(ApiResponse.builder()
                .statusCode(StatusCode.PARAMATER_NOT_FOUND.getCode())
                .message(Objects.requireNonNull(exception.getFieldError()).getDefaultMessage())
                .dateTime(LocalDateTime.now())
                .build());
    }

    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    ResponseEntity<ApiResponse> handlingMethodIllegalStateException(MissingServletRequestParameterException exception) {
        return ResponseEntity.badRequest().body(ApiResponse.builder()
                .statusCode(StatusCode.METHOD_NOT_INSTALLED.getCode())
                .message("Không tìm thấy tham số: " + exception.getParameterName())
                .dateTime(LocalDateTime.now())
                .build());
    }
}