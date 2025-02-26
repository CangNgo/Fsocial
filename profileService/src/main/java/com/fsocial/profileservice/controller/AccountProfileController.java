package com.fsocial.profileservice.controller;

import com.fsocial.profileservice.dto.ApiResponse;
import com.fsocial.profileservice.dto.request.ProfileRegisterRequest;
import com.fsocial.profileservice.dto.request.ProfileUpdateRequest;
import com.fsocial.profileservice.dto.response.ProfileNameResponse;
import com.fsocial.profileservice.dto.response.ProfileResponse;
import com.fsocial.profileservice.dto.response.ProfileUpdateResponse;
import com.fsocial.profileservice.enums.ResponseStatus;
import com.fsocial.profileservice.services.AccountProfileService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AccountProfileController {
    AccountProfileService accountProfileService;

    @PostMapping("/internal/create")
    public ProfileResponse createAccountProfile(@RequestBody @Valid ProfileRegisterRequest request) {
        return accountProfileService.createAccountProfile(request);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{userId}")
    public ApiResponse<ProfileResponse> getAccountProfile(@PathVariable String userId) {
        ProfileResponse response = accountProfileService.getAccountProfileByUserId(userId);
        return buildResponse(response);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{profileId}")
    public ApiResponse<ProfileUpdateResponse> updateProfile(@PathVariable String profileId,
                                                            @RequestBody @Valid ProfileUpdateRequest request) {
        ProfileUpdateResponse response = accountProfileService.updateProfile(profileId, request);
        return buildResponse(response);
    }

    @GetMapping("/internal/{userId}")
    public ProfileNameResponse getProfileByUserId(@PathVariable String userId) {
        return accountProfileService.getProfileByUserId(userId);
    }

    private <T> ApiResponse<T> buildResponse(T data) {
        return ApiResponse.<T>builder()
                .statusCode(ResponseStatus.SUCCESS.getCODE())
                .message(ResponseStatus.SUCCESS.getMessage())
                .dateTime(LocalDateTime.now())
                .data(data)
                .build();
    }
}
