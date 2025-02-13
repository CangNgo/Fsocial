package com.fsocial.accountservice.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    OK(200, "OK", HttpStatus.OK),
    REGISTER_FAILED(444, "Đăng ký không thành công.", HttpStatus.BAD_REQUEST),
    ACCOUNT_EXISTED(464, "Tài khoản đã tồn tại.", HttpStatus.BAD_REQUEST),
    EMAIL_EXISTED(464, "Email đã tồn tại.", HttpStatus.BAD_REQUEST),
    ACCOUNT_NOT_EXISTED(467, "Tài khoản không tồn tại.", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(468, "Tài khoản chưa được xác thực.", HttpStatus.BAD_REQUEST),
    NOT_FOUND(404, "Không tìm thấy.", HttpStatus.NOT_FOUND),
    OTP_INVALID(463, "Mã OTP không hợp lệ.", HttpStatus.BAD_REQUEST),
    USERNAME_EXISTED(470, "Tên đăng nhập đã tồn tại.", HttpStatus.BAD_REQUEST),
    DUPLICATION(471, "Tên đăng nhập hoặc Email đã tồn tại.", HttpStatus.BAD_REQUEST),
    LOGIN_FAILED(471, "Sai tên đăng nhập hoặc mật khẩu.", HttpStatus.BAD_REQUEST),
    INVALID_TYPE_REQUEST(911, "Loại yêu cầu không hợp lệ.", HttpStatus.BAD_REQUEST),
    ;
    final int code;
    final String message;
    final HttpStatusCode statusCode;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}
