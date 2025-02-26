package com.fsocial.accountservice.controller;

import com.fsocial.accountservice.dto.ApiResponse;
import com.fsocial.accountservice.dto.request.account.*;
import com.fsocial.accountservice.dto.response.AccountResponse;
import com.fsocial.accountservice.dto.response.auth.DuplicationResponse;
import com.fsocial.accountservice.enums.ResponseStatus;
import com.fsocial.accountservice.services.AccountService;
import com.fsocial.accountservice.services.OtpService;
import com.fsocial.event.NotificationRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
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
        return buildResponse(null, ResponseStatus.ACCOUNT_REGISTERED);
    }

    @PostMapping("/send-otp")
    public ApiResponse<Void> sendOtp(@RequestBody @Valid EmailRequest request) {
        otpService.sortTypeForSendOtp(request);
        return buildResponse(null, ResponseStatus.OTP_SENT);
    }

    @PostMapping("/verify-otp")
    public ApiResponse<Void> verifyOtp(@RequestBody @Valid OtpRequest request) {
        otpService.sortTypeForVerifyOtp(request);
        return buildResponse(null, ResponseStatus.OTP_VALID);
    }

    @PostMapping("/check-duplication")
    public ResponseEntity<ApiResponse<DuplicationResponse>> checkDuplication(@RequestBody @Valid DuplicationRequest request) {
        ApiResponse<DuplicationResponse> response = accountServices.checkDuplication(request);
        HttpStatus status = response.getStatusCode() != 200 ? HttpStatus.BAD_REQUEST : HttpStatus.OK;
        return ResponseEntity.status(status).body(response);
    }

    @PutMapping("/reset-password")
    public ApiResponse<Void> resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
        accountServices.resetPassword(request.getEmail(), request.getNewPassword());
        return buildResponse(null, ResponseStatus.PASSWORD_RESET_SUCCESS);
    }

    @GetMapping("/{userId}")
    public ApiResponse<AccountResponse> getAccount(@PathVariable String userId) {
        AccountResponse account = accountServices.getUser(userId);
        return buildResponse(account, ResponseStatus.SUCCESS);
    }

    private <T> ApiResponse<T> buildResponse(T data, ResponseStatus responseStatus) {
        return ApiResponse.<T>builder()
                .statusCode(responseStatus.getCODE())
                .message(responseStatus.getMessage())
                .dateTime(LocalDateTime.now())
                .data(data)
                .build();
    }

    @KafkaListener(topics = "notice-comment")
    public void listenNotificationComment(NotificationRequest response) {
        log.info("Response Data: " + response);
    }
}
