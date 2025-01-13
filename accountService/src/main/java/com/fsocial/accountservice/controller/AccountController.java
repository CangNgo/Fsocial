package com.fsocial.accountservice.controller;

import com.fsocial.accountservice.dto.request.AccountRegisterRequest;
import com.fsocial.accountservice.dto.ApiResponse;
import com.fsocial.accountservice.dto.response.AccountResponse;
import com.fsocial.accountservice.exception.StatusCode;
import com.fsocial.accountservice.services.impl.AccountServiceImpl;
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

//    @GetMapping
//    public ResponseEntity<ApiResponse> login() {
//        return ResponseEntity.ok().body(
//                ApiResponse.builder()
//                        .statusCode(StatusCode.OK.getCode())
//                        .message(StatusCode.OK.getMessage())
//                        .data(null)
//                        .build());
//    }

    @PostMapping("/register")
    public ApiResponse<AccountResponse> addAccount(@RequestBody @Valid AccountRegisterRequest accountDTO) {
        return ApiResponse.<AccountResponse>builder()
                .statusCode(StatusCode.OK.getCode())
                .message("Account registration successful")
                .data(accountServices.registerUser(accountDTO))
                .build();

    }

    @GetMapping("/{userId}")
    public ApiResponse<AccountResponse> getAccount(@PathVariable String userId) {
        return ApiResponse.<AccountResponse>builder()
                .statusCode(StatusCode.OK.getCode())
                .message("Get account success.")
                .data(
                        accountServices.getUser(userId)
                )
                .build();
    }
}
