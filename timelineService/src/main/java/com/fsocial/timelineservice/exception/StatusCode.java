package com.fsocial.timelineservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum StatusCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    OK(200, "OK", HttpStatus.OK),
    REGISTER_FAILED(101, "Register failed", HttpStatus.BAD_REQUEST),
    POST_INVALID(222, "POST invalid", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(208, "User Not Found", HttpStatus.NOT_FOUND),
    GET_COMMENT_SUCCESS(207, "Get comment success", HttpStatus.OK),
    PROFILE_NOT_FOUND(208, "Profile Not Found", HttpStatus.NOT_FOUND),
    ;
    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;

    StatusCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}
