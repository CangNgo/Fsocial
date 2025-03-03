package com.fsocial.accountservice.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
public enum ResponseStatus {
    SUCCESS("Thao tác thành công."),
    ACCOUNT_REGISTERED("Tài khoản đã được đăng ký thành công."),
    OTP_SENT("OTP đã được gửi tới email của bạn."),
    OTP_VALID("OTP là hợp lệ."),
    PASSWORD_RESET_SUCCESS("Mật khẩu đã được đặt lại thành công."),
    VALID("Thông tin hợp lệ."),
    INVALID("Thông tin không hợp lệ."),
    PASSWORD_CHANGED("Mật khẩu đã được thay đổi."),
    ;

    private final int CODE = 200;
    private final String message;

}
