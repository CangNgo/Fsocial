package com.fsocial.profileservice.exception;

import com.fsocial.profileservice.dto.ApiResponse;
import com.fsocial.profileservice.enums.ErrorCode;
import com.fsocial.profileservice.enums.ValidErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.Objects;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

//    @ExceptionHandler(value = Exception.class)
//    ResponseEntity<ApiResponse> handlingRuntimeException(RuntimeException exception) {
//
//
//        return ResponseEntity.badRequest().body(ApiResponse.builder()
//                .statusCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode())
//                .message(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage())
//                .dateTime(LocalDateTime.now())
//                .data(null)
//                .build());
//    }
//
//    @ExceptionHandler(value = AppCheckedException.class)
//    ResponseEntity<ApiResponse> handlingAppCheckedException(AppCheckedException exception) {
//        ErrorCode statusCode = exception.getStatus();
//        return ResponseEntity.badRequest().body(ApiResponse.builder()
//                .statusCode(statusCode.getCode())
//                .message(exception.getMessage())
//                .build());
//    }
//
//    @ExceptionHandler(value = NoResourceFoundException.class)
//    ResponseEntity<ApiResponse> handlingNotFoundException(NoResourceFoundException exception) {
//        return ResponseEntity.badRequest().body(ApiResponse.builder()
//                .statusCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode())
//                .message(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage())
//                .build());
//    }
//
//    @ExceptionHandler(value = MethodArgumentNotValidException.class)
//    ResponseEntity<ApiResponse> handlingMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
//        return ResponseEntity.badRequest().body(ApiResponse.builder()
//                .statusCode(exception.getStatusCode().value())
//                .message(Objects.requireNonNull(exception.getFieldError()).getDefaultMessage())
//                .build());
//    }

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse> handlingRuntimeException(RuntimeException exception) {
        log.error("Lỗi ở RuntimeException chưa được xử lý: {}", exception.getMessage());
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
        return buildResponse(ErrorCode.NOT_FOUND);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handleAppException(AppException exception) {
        ErrorCode code = exception.getStatus();
        if (code == null) throw new IllegalArgumentException("Đối tượng không được rỗng.");
        return buildResponse(code);
    }

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