package com.fsocial.profileservice.controller;

import com.fsocial.profileservice.dto.ApiResponse;
import com.fsocial.profileservice.dto.request.AccountProfileRequest;
import com.fsocial.profileservice.dto.response.AccountProfileResponse;
import com.fsocial.profileservice.services.impl.AccountProfileServiceImpl;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountProfileController {
    AccountProfileServiceImpl accountProfileService;

    @PostMapping("/create")
    public ApiResponse<AccountProfileResponse> createAccountProfile(@RequestBody AccountProfileRequest request) {
        return ApiResponse.<AccountProfileResponse>builder()
                .message("Create profile success.")
                .data(accountProfileService.createAccountProfile(request))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<AccountProfileResponse> getAccountProfile(@PathVariable String id) {
        return ApiResponse.<AccountProfileResponse>builder()
                .message("Get profile success.")
                .data(accountProfileService.getAccountProfile(id))
                .build();
    }
}
