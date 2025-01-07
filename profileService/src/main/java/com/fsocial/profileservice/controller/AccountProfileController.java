package com.fsocial.profileservice.controller;

import com.fsocial.profileservice.dto.ApiResponse;
import com.fsocial.profileservice.dto.request.ProfileRegisterRequest;
import com.fsocial.profileservice.dto.request.ProfileUpdateRequest;
import com.fsocial.profileservice.dto.response.ProfileResponse;
import com.fsocial.profileservice.dto.response.ProfileUpdateResponse;
import com.fsocial.profileservice.services.impl.AccountProfileServiceImpl;
import jakarta.validation.Valid;
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
    public ApiResponse<ProfileResponse> createAccountProfile(@RequestBody @Valid ProfileRegisterRequest request) {
        return ApiResponse.<ProfileResponse>builder()
                .message("Create profile success.")
                .data(accountProfileService.createAccountProfile(request))
                .build();
    }

    @GetMapping("/{userId}")
    public ApiResponse<ProfileResponse> getAccountProfile(@PathVariable String userId) {
        return ApiResponse.<ProfileResponse>builder()
                .message("Get profile success.")
                .data(accountProfileService.getAccountProfile(userId))
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
