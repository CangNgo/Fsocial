package com.fsocial.messageservice.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseStatus {
    SUCCESS("Thao tác thành công."),
    VALID("Thông tin hợp lệ."),
    INVALID("Thông tin không hợp lệ."),
    ;

    private final int CODE = 200;
    private final String message;

}
