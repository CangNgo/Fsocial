package com.fsocial.accountservice.controller;

import com.fsocial.accountservice.dto.request.AccountRequest;
import com.fsocial.accountservice.dto.ApiResponse;
import com.fsocial.accountservice.dto.response.AccountResponse;
import com.fsocial.accountservice.exception.AppCheckedException;
import com.fsocial.accountservice.exception.StatusCode;
import com.fsocial.accountservice.services.impl.AccountServiceImpl;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
public class AccountController {

    AccountServiceImpl accountServices;

    @GetMapping
    public ResponseEntity<ApiResponse> login() {
        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .statusCode(StatusCode.OK.getCode())
                        .message(StatusCode.OK.getMessage())
                        .data(null)
                        .build());
    }


    @PostMapping("/register")
    public ApiResponse<AccountResponse> addAccount(@RequestBody @Valid AccountRequest accountDTO) {

        return ApiResponse.<AccountResponse>builder()
                .message("Account registration successful")
                .data(accountServices.registerUser(accountDTO))
                .build();

//        try {
//            AccountRequest result = accountServices.registerUser(accountDTO);
//            return ResponseEntity.ok().body(ApiResponse.builder()
//                    .statusCode(StatusCode.OK.getCode())
//                    .message(StatusCode.OK.getMessage())
//                    .data(result)
//                    .build());
//        } catch (AppCheckedException e) {
//            return ResponseEntity.status(e.getStatus().getCode()).body(ApiResponse.builder()
//                    .statusCode(StatusCode.OK.getCode())
//                    .message(StatusCode.OK.getMessage())
//                    .data(null)
//                    .build());
//        }
    }
}
