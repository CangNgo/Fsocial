package com.fsocial.relationshipService.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum ValidErrorCode{
    UNCATEGORIZED_EXCEPTION(9999, "Lỗi chưa được xử lý."),
    REQUIRED_FIELD(910, "Trường không được để trống."),

    ;
    final int code;
    final String message;
    final HttpStatusCode httpStatusCode = HttpStatus.BAD_REQUEST;

    ValidErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
