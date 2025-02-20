package com.fsocial.accountservice.controller;

import com.fsocial.accountservice.dto.ApiResponse;
import com.fsocial.accountservice.dto.request.account.*;
import com.fsocial.accountservice.dto.response.AccountResponse;
import com.fsocial.accountservice.dto.response.DuplicationResponse;
import com.fsocial.accountservice.enums.ResponseStatus;
import com.fsocial.accountservice.services.AccountService;
import com.fsocial.accountservice.services.OtpService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@Slf4j
public class AccountController {
    AccountService accountServices;
    OtpService otpService;

    @PostMapping("/register")
    public ApiResponse<Void> persistAccount(@RequestBody @Valid AccountRegisterRequest request) {
        accountServices.persistAccount(request);
        return ApiResponse.<Void>builder()
                .statusCode(ResponseStatus.ACCOUNT_REGISTERED.getCODE())
                .message(ResponseStatus.ACCOUNT_REGISTERED.getMessage())
                .dateTime(LocalDateTime.now())
                .build();
    }

    @PostMapping("/send-otp")
    public ApiResponse<Void> sendOtp(@RequestBody @Valid EmailRequest request) {
        otpService.sortTypeForSendOtp(request);
        return ApiResponse.<Void>builder()
                .statusCode(ResponseStatus.OTP_SENT.getCODE())
                .message(ResponseStatus.OTP_SENT.getMessage())
                .dateTime(LocalDateTime.now())
                .build();
    }

    @PostMapping("/verify-otp")
    public ApiResponse<Void> verifyOtp(@RequestBody @Valid OtpRequest request) {
        otpService.sortTypeForVerifyOtp(request);
        return ApiResponse.<Void>builder()
                .statusCode(ResponseStatus.OTP_VALID.getCODE())
                .message(ResponseStatus.OTP_VALID.getMessage())
                .dateTime(LocalDateTime.now())
                .build();
    }

    @PostMapping("/check-duplication")
    public ResponseEntity<ApiResponse<DuplicationResponse>> checkDuplication(@RequestBody @Valid DuplicationRequest request) {
        ApiResponse<DuplicationResponse> response = accountServices.checkDuplication(request);
        HttpStatus status = response.getStatusCode() != 200 ? HttpStatus.BAD_REQUEST : HttpStatus.OK;
        return ResponseEntity.status(status).body(response);
    }

    @PutMapping("/reset-password")
    public ApiResponse<Void> resetPassword(
            @RequestBody @Valid ResetPasswordRequest request) {

        accountServices.resetPassword(
                request.getEmail(),
                request.getNewPassword()
        );

        return ApiResponse.<Void>builder()
                .statusCode(ResponseStatus.PASSWORD_RESET_SUCCESS.getCODE())
                .message(ResponseStatus.PASSWORD_RESET_SUCCESS.getMessage())
                .dateTime(LocalDateTime.now())
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
