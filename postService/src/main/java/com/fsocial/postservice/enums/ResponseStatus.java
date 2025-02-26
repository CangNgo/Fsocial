package com.fsocial.postservice.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
public enum ResponseStatus {
    SUCCESS(200,"Thao tác thành công."),
    ERROR(404, "Thao tác thất bại.")
    ;

    private final int CODE;
    private final String message;
}
