package com.fsocial.postservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum StatusCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    OK(200, "OK", HttpStatus.OK),
    HTTPMETHOD_NOT_SUPPORTED(201, "HTTP method not supported", HttpStatus.NOT_IMPLEMENTED),
    REGISTER_FAILED(101, "Register failed", HttpStatus.BAD_REQUEST),
    CREATE_POST_SUCCESS(100,"Create post success", HttpStatus.OK),
    CREATE_POST_FAILED(211,"Create post success", HttpStatus.BAD_REQUEST),
    UPDATE_POST_SUCCESS(212,"Update post success", HttpStatus.OK),
    DELETE_POST_SUCCESS(213,"Delete post success", HttpStatus.OK),
    FILE_NOT_FOUND(202, "File not found", HttpStatus.NOT_FOUND),
    UPLOAD_FILE_SUCCESS(203, "Upload file success", HttpStatus.OK),
    UPLOAD_FILE_FAILED(204, "Upload file failed", HttpStatus.BAD_REQUEST),
    CREATE_COMMENT_SUCCESS(205, "Create comment success", HttpStatus.OK),
    CREATE_COMMENT_FAILED(206, "Create comment failed", HttpStatus.BAD_REQUEST),
    GET_COMMENT_SUCCESS(207, "Get comment success", HttpStatus.OK),
    USER_NOT_FOUND(208, "Client not found", HttpStatus.NOT_FOUND),
    POST_NOT_FOUND(209  , "Post not found", HttpStatus.NOT_FOUND),
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
