package com.cangngo.apigateway.enums;

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
    UNAUTHENTICATED(468, "Tài khoản chưa được xác thực."),
    NOT_FOUND(404, "Không tìm thấy."),
    TOKEN_EXPIRED(700, "Token hết thời hạn."),
    INVALID_TOKEN(701, "Token không hợp lệ."),
    ACCOUNT_BANNED(601,"Account Banned")
    ;
    final int code;
    final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
