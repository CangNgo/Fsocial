package com.fsocial.timelineservice.exception;

import com.fsocial.timelineservice.dto.Response;
import com.fsocial.timelineservice.enums.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class.getName());
    @ExceptionHandler(value = RuntimeException.class)
    ResponseEntity<Response> handlingRuntimeException(RuntimeException exception) {
        logger.error(exception.getMessage());
        return ResponseEntity.badRequest().body(Response.builder()
                .statusCode(StatusCode.UNCATEGORIZED_EXCEPTION.getCode())
                .message( exception.getMessage())
                .dateTime(LocalDateTime.now())
                .data(null)
                .build());
    }

    @ExceptionHandler(value = AppCheckedException.class)
    ResponseEntity<Response> handlingAppCheckedException(AppCheckedException exception) {
        logger.error(exception.getMessage());
        return ResponseEntity.badRequest().body(Response.builder()
                .statusCode(exception.getStatus().getCode())
                .message(exception.getMessage())
                .build());
    }

    @ExceptionHandler(value = NoResourceFoundException.class)
    ResponseEntity<Response> handlingNotFoundException(NoResourceFoundException exception) {
        logger.error(exception.getMessage());
        return ResponseEntity.badRequest().body(Response.builder()
                .statusCode(StatusCode.ENPOINTMENT_NOT_FOUND.getCode())
                .message("Không tìm thấy enpoint: " + exception.getResourcePath())
                .dateTime(LocalDateTime.now())
                .build());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<Response> handlingMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        logger.error(exception.getMessage());
        return ResponseEntity.badRequest().body(Response.builder()
                .statusCode(StatusCode.PARAMATER_NOT_FOUND.getCode())
                .message(Objects.requireNonNull(exception.getFieldError()).getDefaultMessage())
                .dateTime(LocalDateTime.now())
                .build());
    }

    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    ResponseEntity<Response> handlingMethodIllegalStateException(MissingServletRequestParameterException exception) {
        logger.error("Không tìm thấy tham số: " + exception.getParameterName());
        return ResponseEntity.badRequest().body(Response.builder()
                .statusCode(StatusCode.METHOD_NOT_INSTALLED.getCode())
                .message("Không tìm thấy tham số: " + exception.getParameterName())
                .dateTime(LocalDateTime.now())
                .build());
    }
}
