package com.fsocial.profileservice.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum ValidErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Lỗi chưa được xử lý."),
    INVALID_EMAIL(900, "Email không hợp lệ."),
    INVALID_PASSWORD(902, "Mật khẩu phải có ít nhất 8 ký tự, chứa ít nhất một chữ cái và một chữ số."),
    INVALID_USERNAME(910, "Tên đăng nhập có ít nhất 6 ký tự."),
    INVALID_NAME(913, "Tên không được chứa chữ số."),
    INVALID_DOB(912, "Độ tuổi không phù hợp."),
    INVALID_TYPE_REQUEST(911, "Loại yêu cầu không hợp lệ."),
    REQUIRED_EMAIL(901, "Email không được để trống."),
    REQUIRED_PASSWORD(903, "Mật khẩu không được để trống."),
    REQUIRED_OTP(904, "OTP không được để trống."),
    REQUIRED_USERNAME(905, "Tên đăng nhập không được để trống."),
    REQUIRED_FIRSTNAME(906, "Tên không được để trống."),
    REQUIRED_LASTNAME(907, "Họ không được để trống."),
    REQUIRED_TYPE_REQUEST(908, "Loại yêu cầu không được để trống."),
    REQUIRED_TOKEN(909, "Token không được để trống."),

    ;
    final int code;
    final String message;
    final HttpStatusCode httpStatusCode = HttpStatus.BAD_REQUEST;

    ValidErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
