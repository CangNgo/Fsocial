package com.fsocial.accountservice.enums;

import org.springframework.http.HttpStatusCode;

public interface CodeEnum {
    int getCode();
    String getMessage();
    HttpStatusCode getHttpStatusCode();
}
