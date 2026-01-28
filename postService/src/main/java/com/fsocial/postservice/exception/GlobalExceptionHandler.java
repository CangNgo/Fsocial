package com.fsocial.postservice.exception;

import com.fsocial.postservice.dto.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.Objects;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = RedisConnectionFailureException.class)
    ResponseEntity<Response> handlingRedisConnectionException(RedisConnectionFailureException exception) {
        log.warn("Redis connection failed: {}", exception.getMessage());
        // Trả về empty list hoặc default values thay vì throw exception
        return ResponseEntity.ok().body(Response.builder()
                .statusCode(StatusCode.OK.getCode())
                .message("Redis temporarily unavailable, using default values")
                .dateTime(LocalDateTime.now())
                .data(null)
                .build());
    }

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<Response> handlingRuntimeException(Exception exception) {
        log.error("Unhandled exception: {}", exception.getMessage(), exception);
        return ResponseEntity.badRequest().body(Response.builder()
                .statusCode(StatusCode.UNCATEGORIZED_EXCEPTION.getCode())
                .message(exception.getMessage())
                .dateTime(LocalDateTime.now())
                .data(null)
                .build());
    }

    @ExceptionHandler(value = AppCheckedException.class)
    ResponseEntity<Response> handlingAppCheckedException(AppCheckedException exception) {
        return ResponseEntity.badRequest().body(Response.builder()
                .statusCode(exception.getStatus().getCode())
                .message(exception.getMessage()).dateTime(LocalDateTime.now())
                .build());
    }

    @ExceptionHandler(value = NoResourceFoundException.class)
    ResponseEntity<Response> handlingNotFoundException(NoResourceFoundException exception) {
        return ResponseEntity.badRequest().body(Response.builder()
                .statusCode(StatusCode.ENPOINTMENT_NOT_FOUND.getCode())
                .message("Không tìm thấy enpoint: " + exception.getResourcePath())
                .dateTime(LocalDateTime.now())
                .build());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<Response> handlingMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        return ResponseEntity.badRequest().body(Response.builder()
                .statusCode(StatusCode.PARAMATER_NOT_FOUND.getCode())
                .message(Objects.requireNonNull(exception.getFieldError()).getDefaultMessage())
                .build());
    }

    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    ResponseEntity<Response> handlingMethodIllegalStateException(MissingServletRequestParameterException exception) {
        return ResponseEntity.badRequest().body(Response.builder()
                .statusCode(StatusCode.METHOD_NOT_INSTALLED.getCode())
                .message("Không tìm thấy tham số: " + exception.getParameterName())
                .dateTime(LocalDateTime.now())
                .build());
    }

    @ExceptionHandler(value = AppUnCheckedException.class)
    ResponseEntity<Response> handleAppException(AppUnCheckedException exception) {
        StatusCode code = exception.getStatus();
        if (code == null) throw new IllegalArgumentException("Đối tượng không được rỗng.");
        return buildResponse(code);
    }

    private ResponseEntity<Response> buildResponse(StatusCode errorCode) {
        return ResponseEntity.status(errorCode.getCode())
                .body(Response.builder()
                        .statusCode(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .dateTime(LocalDateTime.now())
                        .build());
    }
}
