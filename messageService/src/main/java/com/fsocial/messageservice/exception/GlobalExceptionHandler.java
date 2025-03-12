package com.fsocial.messageservice.exception;

import com.fsocial.messageservice.dto.ApiResponse;
import com.fsocial.messageservice.enums.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse> handlingRuntimeException(RuntimeException exception) {
        log.error("Lỗi ở RuntimeException chưa được xử lý: {}", exception.getMessage());
        return buildResponse(ErrorCode.UNCATEGORIZED_EXCEPTION);
    }

    @ExceptionHandler(value = AppCheckedException.class)
    ResponseEntity<ApiResponse> handlingAppCheckedException(AppCheckedException exception) {
        return buildResponse(exception.getStatus());
    }

    @ExceptionHandler(value = NoResourceFoundException.class)
    ResponseEntity<ApiResponse> handlingNotFoundException(NoResourceFoundException exception) {
        return buildResponse(ErrorCode.NOT_FOUND);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handleAppException(AppException exception) {
        ErrorCode code = exception.getErrorCode();
        if (code == null) throw new IllegalArgumentException("Đối tượng không được rỗng.");
        return buildResponse(code);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiResponse> handleAccessDeniedException(AccessDeniedException exception) {
        return buildResponse(ErrorCode.UNAUTHENTICATED);
    }

    @ExceptionHandler(value = AuthorizationDeniedException.class)
    ResponseEntity<ApiResponse> handleAuthorizationDeniedException(AuthorizationDeniedException exception) {
        return buildResponse(ErrorCode.UNAUTHORIZED);
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