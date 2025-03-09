package com.fsocial.messageservice.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Lỗi chưa được xử lí.", HttpStatus.INTERNAL_SERVER_ERROR),
    OK(200, "OK", HttpStatus.OK),
    UNAUTHENTICATED(468, "Tài khoản chưa được xác thực.", HttpStatus.BAD_REQUEST),
    NOT_FOUND(404, "Không tìm thấy.", HttpStatus.NOT_FOUND),
    UNAUTHORIZED(703, "Không có quyền truy cập.", HttpStatus.UNAUTHORIZED),
    ACCOUNT_NOT_EXISTED(467, "Tài khoản không tồn tại.", HttpStatus.BAD_REQUEST),
    CONVERSATION_NOT_EXIST(474, "Cuộc trò chuyện không tồn tại.", HttpStatus.BAD_REQUEST),
    PROFILE_NOT_EXISTED(476, "Hồ sơ thông tin không tồn tại.", HttpStatus.BAD_REQUEST),
    CONVERSATION_EXISTED(473, "Cuộc trò chuyện đã tồn tại.", HttpStatus.BAD_REQUEST),
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
