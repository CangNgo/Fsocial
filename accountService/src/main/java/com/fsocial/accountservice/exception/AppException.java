package com.fsocial.accountservice.exception;

import com.fsocial.accountservice.enums.ErrorCode;
import com.fsocial.accountservice.enums.ValidCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

/*
AppUnCheckedException là một unchecked exception, được sử dụng để xử lý các lỗi trong
runtime của ứng dụng của bạn mà không yêu cầu phải khai báo throws
 hoặc bắt buộc xử lý bằng khối try-catch.
* */
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppException extends RuntimeException {
    final ErrorCode statusCode;

    public AppException(ErrorCode statusCode) {
        super(statusCode.getMessage());
        this.statusCode = statusCode;
    }
}
