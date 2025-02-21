package com.fsocial.accountservice.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum ErrorCode{
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error"),
    OK(200, "OK"),
    REGISTER_FAILED(444, "Đăng ký không thành công."),
    ACCOUNT_EXISTED(464, "Tài khoản đã tồn tại."),
    EMAIL_EXISTED(464, "Email đã tồn tại."),
    ACCOUNT_NOT_EXISTED(467, "Tài khoản không tồn tại."),
    UNAUTHENTICATED(468, "Tài khoản chưa được xác thực."),
    NOT_FOUND(404, "Không tìm thấy."),
    OTP_INVALID(463, "Mã OTP không hợp lệ."),
    USERNAME_EXISTED(470, "Tên đăng nhập đã tồn tại."),
    DUPLICATION(471, "Tên đăng nhập hoặc Email đã tồn tại."),
    LOGIN_FAILED(472, "Sai tên đăng nhập hoặc mật khẩu."),
    WEAK_SECRET_KEY(473, "Signer Key không đủ mạnh."),
    TOKEN_EXPIRED(700, "Token hết thời hạn."),
    INVALID_TOKEN(701, "Token không hợp lệ."),
    ENPOINTMENT_NOT_FOUND(210, "Enpointment Not Found"),
    PARAMATER_NOT_FOUND(220, "Paramater Not Found"),
    METHOD_NOT_INSTALLED(230, "Method Not installed"),
    ;
    final int code;
    final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
