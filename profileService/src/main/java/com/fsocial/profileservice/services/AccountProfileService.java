package com.fsocial.profileservice.services;

import com.fsocial.profileservice.dto.request.ProfileRegisterRequest;
import com.fsocial.profileservice.dto.request.ProfileUpdateRequest;
import com.fsocial.profileservice.dto.response.ProfileNameResponse;
import com.fsocial.profileservice.dto.response.ProfilePageResponse;
import com.fsocial.profileservice.dto.response.ProfileResponse;
import com.fsocial.profileservice.dto.response.ProfileUpdateResponse;

public interface AccountProfileService {
    ProfileResponse createAccountProfile(ProfileRegisterRequest request);
    ProfileResponse getAccountProfileByUserId(String userId);
    ProfileUpdateResponse updateProfile(String userId, ProfileUpdateRequest request);
    ProfileNameResponse getProfileNameByUserId(String userId);
    ProfilePageResponse getProfilePageByUserId(String userId);
}
