package com.fsocial.accountservice.exception;

import com.fsocial.accountservice.dto.Response];
import com.fsocial.accountservice.enums.ErrorCode;
import com.fsocial.accountservice.enums.ValidErrorCode;
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
    ResponseEntity<Response> handlingRuntimeException(RuntimeException exception) {
        log.error("Lỗi ở RuntimeException chưa được xử lý: {}", exception.getMessage());
        return ResponseEntity.internalServerError().body(Response].builder()
                .statusCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode())
                .message(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage())
                .dateTime(LocalDateTime.now())
                .build());
    }

    @ExceptionHandler(value = AppCheckedException.class)
    ResponseEntity<Response> handlingAppCheckedException(AppCheckedException exception) {
        return ResponseEntity
                .badRequest()
                .body(Response].builder()
                        .statusCode(exception.getStatus().getCode())
                        .message(exception.getStatus().getMessage())
                        .dateTime(LocalDateTime.now())
                        .build());
    }

    @ExceptionHandler(value = NoResourceFoundException.class)
    ResponseEntity<Response> handlingNotFoundException(NoResourceFoundException exception) {
        return buildResponse(ErrorCode.NOT_FOUND);
    }

    @ExceptionHandler(value = AppException.class)
     ResponseEntity<Response> handleAppException(AppException exception) {
        ErrorCode code = exception.getCode();
        if (code == null) throw new IllegalArgumentException("Đối tượng không được rỗng.");
        return buildResponse(code);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<Response> handleValidException(MethodArgumentNotValidException exception) {
        String enumKey = Objects.requireNonNull(exception.getFieldError()).getDefaultMessage();
        ValidErrorCode errorCode = ValidErrorCode.valueOf(enumKey);
        return ResponseEntity.badRequest().body(Response].builder()
                .statusCode(errorCode.getCode())
                .message(errorCode.getMessage())
                .dateTime(LocalDateTime.now())
                .build());
    }
}
