package com.fsocial.timelineservice.enums;

import lombok.Getter;

@Getter
public enum StatusCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error"),
    OK(200, "OK"),
    HTTPMETHOD_NOT_SUPPORTED(201, "HTTP method not supported"),
    REGISTER_FAILED(101, "Register failed"),
    CREATE_POST_SUCCESS(100,"Create post success"),
    CREATE_POST_FAILED(211,"Create post success"),
    UPDATE_POST_SUCCESS(212,"Update post success"),
    DELETE_POST_SUCCESS(213,"Delete post success"),
    FILE_NOT_FOUND(202, "File not found"),
    UPLOAD_FILE_SUCCESS(203, "Upload file success"),
    UPLOAD_FILE_FAILED(204, "Upload file failed"),
    CREATE_COMMENT_SUCCESS(205, "Create comment success"),
    CREATE_COMMENT_FAILED(206, "Create comment failed"),
    GET_COMMENT_SUCCESS(207, "Get comment success"),
    USER_NOT_FOUND(208, "Client not found"),
    POST_NOT_FOUND(209  , "Post not found"),
    ENPOINTMENT_NOT_FOUND(210, "Enpointment Not Found"),
    PARAMATER_NOT_FOUND(220, "Paramater Not Found"),
    METHOD_NOT_INSTALLED(230, "Method Not installed"),
    INTERNAL_SERVER_ERROR(300, "Internal Server Error"),
    UNSUPPORTED_MEDIA_TYPE(304, "Unsupported Media Type"),
    UNAUTHENTICATED(468, "Tài khoản chưa được xác thực."),
    PROFILE_NOT_FOUND(400, "Profile not found"),

    ;
    private final int code;
    private final String message;

    StatusCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
