package com.fsocial.profileservice.exception;

import com.fsocial.profileservice.enums.ErrorCode;
import lombok.Getter;

/*
AppUnCheckedException là một unchecked exception, được sử dụng để xử lý các lỗi trong
runtime của ứng dụng của bạn mà không yêu cầu phải khai báo throws
 hoặc bắt buộc xử lý bằng khối try-catch.
* */
@Getter
public class AppException extends RuntimeException {
    private final ErrorCode status;

    public AppException(ErrorCode status) {
        super(status.getMessage());
        this.status = status;
    }
}
