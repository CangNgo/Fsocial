package com.fsocial.postservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/*
AppUnCheckedException là một unchecked exception, được sử dụng để xử lý các lỗi trong
runtime của ứng dụng của bạn mà không yêu cầu phải khai báo throws
 hoặc bắt buộc xử lý bằng khối try-catch.
* */
@Getter
public class AppUnCheckedException extends RuntimeException {
    private final StatusCode status;

    public AppUnCheckedException(String message, StatusCode status) {
        super(message);
        this.status = status;
    }
}
