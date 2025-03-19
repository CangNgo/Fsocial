package com.fsocial.profileservice.controller;

import com.fsocial.profileservice.dto.request.ProfileRegisterRequest;
import com.fsocial.profileservice.dto.response.ProfileNameResponse;
import com.fsocial.profileservice.dto.response.ProfileResponse;
import com.fsocial.profileservice.services.AccountProfileService;
import com.fsocial.profileservice.services.ProfileService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PACKAGE, makeFinal = true)
@RequestMapping("/internal")
public class APIProfileInternal {
    AccountProfileService accountProfileService;
    ProfileService profileService;

    @PostMapping("/create")
    public ProfileResponse createAccountProfile(@RequestBody @Valid ProfileRegisterRequest request) {
        return accountProfileService.createAccountProfile(request);
    }

    @GetMapping("/{userId}")
    public ProfileNameResponse getProfileByUserId(@PathVariable String userId) {
        return accountProfileService.getProfileNameByUserId(userId);
    }

    @GetMapping("/message/{userId}")
    public ProfileResponse getAccountProfileFromAnotherService(@PathVariable String userId) {
        return accountProfileService.getAccountProfileByUserId(userId);
    }
}
