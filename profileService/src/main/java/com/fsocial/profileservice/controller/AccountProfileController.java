package com.fsocial.profileservice.controller;

import com.fsocial.profileservice.dto.ApiResponse;
import com.fsocial.profileservice.dto.request.ProfileRegisterRequest;
import com.fsocial.profileservice.dto.request.ProfileUpdateRequest;
import com.fsocial.profileservice.dto.response.*;
import com.fsocial.profileservice.enums.ProfileVisibility;
import com.fsocial.profileservice.enums.ResponseStatus;
import com.fsocial.profileservice.exception.AppCheckedException;
import com.fsocial.profileservice.services.AccountProfileService;
import com.fsocial.profileservice.services.ProfileService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AccountProfileController {
    AccountProfileService accountProfileService;
    ProfileService profileService;

    @GetMapping("/external/{userIdByPost}")
    public ProfileResponse getProfileResponseByUserId(@PathVariable("userIdByPost") String useridByPost) {
        return accountProfileService.getAccountProfileByUserId(useridByPost);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public ApiResponse<ProfileResponse> getAccountProfile() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        ProfileResponse response = accountProfileService.getAccountProfileByUserId(userId);
        return ApiResponse.buildApiResponse(response, ResponseStatus.SUCCESS);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping(value = "/update")
    public ApiResponse<ProfileUpdateResponse> updateProfile(ProfileUpdateRequest request) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        ProfileUpdateResponse response = accountProfileService.updateProfile(userId, request);
        return ApiResponse.buildApiResponse(response, ResponseStatus.SUCCESS);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/profile-page")
    public ApiResponse<ProfilePageResponse> getProfilePage() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        ProfilePageResponse response = accountProfileService.getProfilePageByUserId(userId);
        return ApiResponse.buildApiResponse(response, ResponseStatus.SUCCESS);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/profile-page/{userId}/other")
    public ApiResponse<ProfilePageOtherResponse> getProfilePageOther(@PathVariable String userId) {
        String ownerId = SecurityContextHolder.getContext().getAuthentication().getName();
        ProfilePageOtherResponse response = accountProfileService.getProfilePageOther(ownerId, userId);
        return ApiResponse.buildApiResponse(response, ResponseStatus.SUCCESS);
    }

    @GetMapping("/{profileId}")
    public ApiResponse<ProfileAdminResponse> getProfileAdmin(@PathVariable String profileId) throws AppCheckedException {
        ProfileAdminResponse response  = profileService.getProfileAdmin(profileId);
        return ApiResponse.buildApiResponse(response, ResponseStatus.SUCCESS);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/update-visibility/{userId}")
    public ApiResponse<UpdatePrivacyResponse> updateProfileVisibility() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        return ApiResponse.buildApiResponse(accountProfileService.updateProfileVisibility(userId), ResponseStatus.SUCCESS);
    }
}
