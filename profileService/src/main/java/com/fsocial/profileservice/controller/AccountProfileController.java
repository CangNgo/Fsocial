package com.fsocial.profileservice.controller;

import com.fsocial.profileservice.dto.ApiResponse;
import com.fsocial.profileservice.dto.request.ProfileRegisterRequest;
import com.fsocial.profileservice.dto.request.ProfileUpdateRequest;
import com.fsocial.profileservice.dto.response.ProfileAdminResponse;
import com.fsocial.profileservice.dto.response.ProfileNameResponse;
import com.fsocial.profileservice.dto.response.ProfilePageResponse;
import com.fsocial.profileservice.dto.response.ProfileResponse;
import com.fsocial.profileservice.dto.response.ProfileUpdateResponse;
import com.fsocial.profileservice.enums.ResponseStatus;
import com.fsocial.profileservice.exception.AppCheckedException;
import com.fsocial.profileservice.services.AccountProfileService;
import com.fsocial.profileservice.services.ProfileService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AccountProfileController {
    AccountProfileService accountProfileService;

    ProfileService profileService;

    @PostMapping("/internal/create")
    public ProfileResponse createAccountProfile(@RequestBody @Valid ProfileRegisterRequest request) {
        return accountProfileService.createAccountProfile(request);
    }

    @GetMapping("/internal/{userId}")
    public ProfileNameResponse getProfileByUserId(@PathVariable String userId) {
        return accountProfileService.getProfileNameByUserId(userId);
    }

    @GetMapping("/internal/message/{userId}")
    public ProfileResponse getAccountProfileFromAnotherService(@PathVariable String userId) {
        return accountProfileService.getAccountProfileByUserId(userId);
    }

//    @GetMapping("/external/{userIdByPost}")
//    public ProfileResponse getProfileResponseByUserId(@PathVariable("userIdByPost") String useridByPost) {
//        return accountProfileService.getAccountProfile(useridByPost);
//    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public ApiResponse<ProfileResponse> getAccountProfile() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        ProfileResponse response = accountProfileService.getAccountProfileByUserId(userId);
        return buildResponse(response);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/update")
    public ApiResponse<ProfileUpdateResponse> updateProfile(@RequestBody ProfileUpdateRequest request) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        ProfileUpdateResponse response = accountProfileService.updateProfile(userId, request);
        return buildResponse(response);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/profile-page")
    public ApiResponse<ProfilePageResponse> getProfilePage() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        ProfilePageResponse response = accountProfileService.getProfilePageByUserId(userId);
        return buildResponse(response);
    }

    private <T> ApiResponse<T> buildResponse(T data) {
        return ApiResponse.<T>builder()
                .statusCode(ResponseStatus.SUCCESS.getCODE())
                .message(ResponseStatus.SUCCESS.getMessage())
                .dateTime(LocalDateTime.now())
                .data(data)
                .build();
    }

    @GetMapping("/{profileId}")
    public ApiResponse<ProfileAdminResponse> getProfileAdmin(@PathVariable String profileId) throws AppCheckedException {
        ProfileAdminResponse response  = profileService.getProfileAdmin(profileId);
        return buildResponse(response);
    }
}
