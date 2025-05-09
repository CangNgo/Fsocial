package com.fsocial.timelineservice.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResponseStatus {
    SUCCESS("Thao tác thành công."),
    VALID("Thông tin hợp lệ."),
    INVALID("Thông tin không hợp lệ.")
    ;

    private final int CODE = 200;
    private final String message;

}
