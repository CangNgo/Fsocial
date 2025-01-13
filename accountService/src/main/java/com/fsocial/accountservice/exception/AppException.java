package com.fsocial.accountservice.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

/*
AppUnCheckedException là một unchecked exception, được sử dụng để xử lý các lỗi trong
runtime của ứng dụng của bạn mà không yêu cầu phải khai báo throws
 hoặc bắt buộc xử lý bằng khối try-catch.
* */
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppException extends RuntimeException {
    final StatusCode statusCode;

    public AppException(StatusCode statusCode) {
        super(statusCode.getMessage());
        this.statusCode = statusCode;
    }
}
