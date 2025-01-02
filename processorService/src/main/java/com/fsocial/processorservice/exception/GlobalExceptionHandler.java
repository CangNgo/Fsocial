package com.fsocial.processorservice.exception;

import com.fsocial.accountservice.dto.Response;
import com.fsocial.accountservice.exception.StatusCode;
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
    ResponseEntity<Response> handlingRuntimeException(RuntimeException exception) {

        return ResponseEntity.badRequest().body(Response.builder()
                .statusCode(StatusCode.UNCATEGORIZED_EXCEPTION.getCode())
                .message(StatusCode.UNCATEGORIZED_EXCEPTION.getMessage())
                .dateTime(LocalDateTime.now())
                .data(null)
                .build());
    }

    @ExceptionHandler(value = AppCheckedException.class)
    ResponseEntity<Response> handlingAppCheckedException(AppCheckedException exception) {
        StatusCode statusCode = exception.getStatus();
        return ResponseEntity.badRequest().body(Response.builder()
                .statusCode(statusCode.getCode())
                .message(exception.getMessage())
                .build());
    }

    @ExceptionHandler(value = NoResourceFoundException.class)
    ResponseEntity<Response> handlingNotFoundException(NoResourceFoundException exception) {
        return ResponseEntity.badRequest().body(Response.builder()
                .statusCode(StatusCode.UNCATEGORIZED_EXCEPTION.getCode())
                .message(StatusCode.UNCATEGORIZED_EXCEPTION.getMessage())
                .build());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<Response> handlingMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        return ResponseEntity.badRequest().body(Response.builder()
                .statusCode(exception.getStatusCode().value())
                .message(Objects.requireNonNull(exception.getFieldError()).getDefaultMessage())
                .build());
    }
}
