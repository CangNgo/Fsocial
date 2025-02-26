package com.fsocial.postservice.exception;

import com.fsocial.postservice.dto.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
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
                .message("Lỗi gì đó mà chúng tôi cũng không biết hihi :D" + exception.getMessage())
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

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handleAppException(AppException exception) {
        ErrorCode code = exception.getErrorCode();
        if (code == null) throw new IllegalArgumentException("Đối tượng không được rỗng.");
        return buildResponse(code);
    }

    private ResponseEntity<ApiResponse> buildResponse(ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getStatusCode())
                .body(ApiResponse.builder()
                        .statusCode(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .dateTime(LocalDateTime.now())
                        .build());
    }
}
