package com.fsocial.profileservice.services;

import com.fsocial.profileservice.dto.request.ProfileRegisterRequest;
import com.fsocial.profileservice.dto.request.ProfileUpdateRequest;
import com.fsocial.profileservice.dto.response.ProfileNameResponse;
import com.fsocial.profileservice.dto.response.ProfileResponse;
import com.fsocial.profileservice.dto.response.ProfileUpdateResponse;

public interface AccountProfileService {
    ProfileResponse createAccountProfile(ProfileRegisterRequest request);
    ProfileResponse getAccountProfileByUserId();
    ProfileUpdateResponse updateProfile(String profileId, ProfileUpdateRequest request);
    ProfileNameResponse getProfileByUserId(String userId);
}
