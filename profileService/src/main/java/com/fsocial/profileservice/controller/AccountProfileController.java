package com.fsocial.profileservice.controller;

import com.fsocial.profileservice.dto.ApiResponse;
import com.fsocial.profileservice.dto.request.ProfileRegisterRequest;
import com.fsocial.profileservice.dto.request.ProfileUpdateRequest;
import com.fsocial.profileservice.dto.response.ProfileResponse;
import com.fsocial.profileservice.dto.response.ProfileUpdateResponse;
import com.fsocial.profileservice.enums.ResponseStatus;
import com.fsocial.profileservice.services.impl.AccountProfileServiceImpl;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AccountProfileController {
    AccountProfileServiceImpl accountProfileService;

    @PostMapping("/internal/create")
    public ProfileResponse createAccountProfile(@RequestBody @Valid ProfileRegisterRequest request) {
        return accountProfileService.createAccountProfile(request);
    }

    @GetMapping("/{userId}")
    public ApiResponse<ProfileResponse> getAccountProfile(@PathVariable String userId) {
        return ApiResponse.<ProfileResponse>builder()
                .statusCode(ResponseStatus.SUCCESS.getCODE())
                .message(ResponseStatus.SUCCESS.getMessage())
                .dateTime(LocalDateTime.now())
                .data(accountProfileService.getAccountProfileByUserId(userId))
                .build();
    }

    @PutMapping("/{profileId}")
    public ApiResponse<ProfileUpdateResponse> updateProfile(@PathVariable String profileId,
                                                            @RequestBody @Valid ProfileUpdateRequest request) {
        return ApiResponse.<ProfileUpdateResponse>builder()
                .message("Update profile success.")
                .data(
                        accountProfileService.updateProfile(profileId, request)
                )
                .build();
    }
}
