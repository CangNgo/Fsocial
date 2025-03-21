package com.fsocial.profileservice.services;

import com.fsocial.profileservice.dto.request.ProfileRegisterRequest;
import com.fsocial.profileservice.dto.request.ProfileUpdateRequest;
import com.fsocial.profileservice.dto.response.*;
import com.fsocial.profileservice.enums.ProfileVisibility;

public interface AccountProfileService {
    ProfileResponse createAccountProfile(ProfileRegisterRequest request);
    ProfileResponse getAccountProfileByUserId(String userId);
    ProfileUpdateResponse updateProfile(String userId, ProfileUpdateRequest request);
    ProfileNameResponse getProfileNameByUserId(String userId);
    ProfilePageResponse getProfilePageByUserId(String userId);
    ProfilePageOtherResponse getProfilePageOther(String ownerId, String userId);
    UpdatePrivacyResponse updateProfileVisibility(String userId);
}
