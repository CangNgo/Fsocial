package com.notification_service.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Lỗi chưa được xử lý.", HttpStatus.INTERNAL_SERVER_ERROR),
    OK(200, "OK", HttpStatus.OK),
    INVALID_TOKEN(701, "Token không hợp lệ.", HttpStatus.BAD_REQUEST),
    NOT_FOUND(404, "Không tìm thấy.", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(468, "Tài khoản chưa được xác thực.", HttpStatus.BAD_REQUEST),
    ;
    final int code;
    final String message;
    final HttpStatusCode httpStatusCode;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.httpStatusCode = statusCode;
    }
}
