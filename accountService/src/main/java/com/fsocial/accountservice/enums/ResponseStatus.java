package com.fsocial.accountservice.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResponseStatus {
    SUCCESS("Success"),
    ACCOUNT_REGISTERED("Account registered successfully."),
    OTP_SENT("OTP has been sent to your email."),
    OTP_VALID("OTP is valid."),
    PASSWORD_RESET_SUCCESS("Password has been reset successfully.")
    ;

    private final int CODE = 200;
    private final String message;

}
