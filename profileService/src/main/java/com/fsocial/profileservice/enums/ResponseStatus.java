package com.fsocial.profileservice.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResponseStatus {
    SUCCESS("Thao tác thành công."),
    ACCOUNT_REGISTERED("Tài khoản đã được đăng ký thành công."),
    OTP_SENT("OTP đã được gửi tới email của bạn."),
    OTP_VALID("OTP là hợp lệ."),
    PASSWORD_RESET_SUCCESS("Mật khẩu đã được đặt lại thành công."),
    VALID("Thông tin hợp lệ."),
    INVALID("Thông tin không hợp lệ.")
    ;

    private final int CODE = 200;
    private final String message;

}
