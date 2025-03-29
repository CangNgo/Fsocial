package com.cangngo.apigateway.exception;

import com.cangngo.apigateway.dto.Response;
import com.cangngo.apigateway.enums.StatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = RuntimeException.class)
    ResponseEntity<Response> handlingRuntimeException(RuntimeException exception) {

        return ResponseEntity.badRequest().body(Response.builder()
                .statusCode(StatusCode.BANNED.getCode())
                .message(exception.getMessage())
                .dateTime(LocalDateTime.now())
                .data(null)
                .build());
    }
}
