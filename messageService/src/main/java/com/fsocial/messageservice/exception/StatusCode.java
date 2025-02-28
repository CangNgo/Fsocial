package com.fsocial.messageservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum StatusCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error"),
    OK(200, "OK"),
    REGISTER_FAILED(101, "Register failed"),
    ENPOINTMENT_NOT_FOUND(210, "Enpointment Not Found"),
    PARAMATER_NOT_FOUND(220, "Paramater Not Found"),
    METHOD_NOT_INSTALLED(230, "Method Not installed"),
    ;
    private final int code;
    private final String message;

    StatusCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
