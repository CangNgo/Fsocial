package com.fsocial.accountservice.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum StatusCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    OK(200, "OK", HttpStatus.OK),
    REGISTER_FAILED(101, "Register failed", HttpStatus.BAD_REQUEST),
    ACCOUNT_EXISTED(464, "Account already exists", HttpStatus.BAD_REQUEST),
    EMAIL_EXISTED(464, "Email already exists", HttpStatus.BAD_REQUEST),
    NOT_EXIST(467, "Account not existed.", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(468, "Unauthenticated.", HttpStatus.BAD_REQUEST),
    LOGIN_FAIL(202,"Incorrect account or password.", HttpStatus.BAD_REQUEST),
    NOT_FOUND(404, "Not Found.", HttpStatus.NOT_FOUND),
    OTP_INVALID(463, "Invalid OTP code", HttpStatus.BAD_REQUEST),
    USERNAME_EXISTED(470, "Username already exists", HttpStatus.BAD_REQUEST)
    ;
    final int code;
    final String message;
    final HttpStatusCode statusCode;

    StatusCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}
