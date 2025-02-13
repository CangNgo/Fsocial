package com.fsocial.accountservice.controller;

import com.fsocial.accountservice.dto.ApiResponse;
import com.fsocial.accountservice.dto.request.account.*;
import com.fsocial.accountservice.dto.response.AccountResponse;
import com.fsocial.accountservice.dto.response.DuplicationResponse;
import com.fsocial.accountservice.enums.ResponseStatus;
import com.fsocial.accountservice.services.impl.AccountServiceImpl;
import com.fsocial.accountservice.services.impl.OtpServiceImpl;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
public class AccountController {
    AccountServiceImpl accountServices;
    OtpServiceImpl otpService;

    String KEY_PREFIX_REGIS = "REGIS_";
    String KEY_PREFIX_RESET = "RESET_";

    @PostMapping("/register")
    public ApiResponse<Void> persistAccount(@RequestBody @Valid AccountRegisterRequest request) {
        accountServices.persistAccount(request);
        return ApiResponse.<Void>builder()
                .statusCode(ResponseStatus.ACCOUNT_REGISTERED.getCODE())
                .message(ResponseStatus.ACCOUNT_REGISTERED.getMessage())
                .build();
    }

    @PostMapping("/send-otp")
    public ApiResponse<Void> sendOtp(@RequestBody EmailRequest request) {
        if ("REGISTER".equals(request.getType())) {
            otpService.sendOtp(request.getEmail(), KEY_PREFIX_REGIS);
        } else {
            otpService.sendOtp(request.getEmail(), KEY_PREFIX_RESET);
        }
        return ApiResponse.<Void>builder()
                .statusCode(ResponseStatus.OTP_SENT.getCODE())
                .message(ResponseStatus.OTP_SENT.getMessage())
                .build();
    }

    @PostMapping("/verify-otp")
    public ApiResponse<Void> verifyOtp(@RequestBody OtpRequest request) {
        if ("REGISTER".equals(request.getType())) {
            otpService.validateOtp(request.getEmail(), request.getOtp(), KEY_PREFIX_REGIS);
        } else {
            otpService.validateOtp(request.getEmail(), request.getOtp(), KEY_PREFIX_RESET);
        }
        return ApiResponse.<Void>builder()
                .statusCode(ResponseStatus.OTP_VALID.getCODE())
                .message(ResponseStatus.OTP_VALID.getMessage())
                .build();
    }

    @PostMapping("/check-duplication")
    public ApiResponse<DuplicationResponse> checkDuplication(@RequestBody DuplicationRequest request) {
        return ApiResponse.<DuplicationResponse>builder()
                .statusCode(ResponseStatus.VALID.getCODE())
                .message(ResponseStatus.VALID.getMessage())
                .data(
                        accountServices.checkDuplication(request)
                )
                .build();
    }

    @PutMapping("/reset-password")
    public ApiResponse<Void> resetPassword(
            @RequestBody ResetPasswordRequest request) {

        accountServices.resetPassword(
                request.getEmail(),
                request.getOtp(),
                request.getNewPassword()
        );

        return ApiResponse.<Void>builder()
                .statusCode(ResponseStatus.PASSWORD_RESET_SUCCESS.getCODE())
                .message(ResponseStatus.PASSWORD_RESET_SUCCESS.getMessage())
                .build();
    }

    @GetMapping("/{userId}")
    public ApiResponse<AccountResponse> getAccount(@PathVariable String userId) {
        AccountResponse account = accountServices.getUser(userId);
        return ApiResponse.<AccountResponse>builder()
                .statusCode(ResponseStatus.SUCCESS.getCODE())
                .message(ResponseStatus.SUCCESS.getMessage())
                .data(
                       account
                )
                .build();
    }
}
