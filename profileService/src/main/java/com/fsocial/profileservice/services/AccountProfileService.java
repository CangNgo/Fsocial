package com.fsocial.profileservice.services;

import com.fsocial.profileservice.dto.request.ProfileRegisterRequest;
import com.fsocial.profileservice.dto.request.ProfileUpdateRequest;
import com.fsocial.profileservice.dto.response.*;
import org.springframework.web.multipart.MultipartFile;

public interface AccountProfileService {
    ProfileResponse createAccountProfile(ProfileRegisterRequest request);
    ProfileResponse getAccountProfileByUserId(String userId);
    ProfileUpdateResponse updateProfile(String userId, ProfileUpdateRequest request);
    ProfileNameResponse getProfileNameByUserId(String userId);
    ProfilePageResponse getProfilePageByUserId(String userId);
    ProfilePageOtherResponse getProfilePageOther(String ownerId, String userId);
    UpdatePrivacyResponse updateProfileVisibility(String userId);
    void updateProfileImage(String userId, MultipartFile file, boolean isAvatar);
}
