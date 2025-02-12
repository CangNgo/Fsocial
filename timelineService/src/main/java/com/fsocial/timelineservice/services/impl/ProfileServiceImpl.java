package com.fsocial.timelineservice.services.impl;

import com.fsocial.timelineservice.Repository.httpClient.ProfileClient;
import com.fsocial.timelineservice.dto.ApiResponse;
import com.fsocial.timelineservice.dto.profile.ProfileResponse;
import com.fsocial.timelineservice.exception.AppCheckedException;
import com.fsocial.timelineservice.exception.StatusCode;
import com.fsocial.timelineservice.services.ProfleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProfileServiceImpl implements ProfleService {

    ProfileClient profileClient;

    @Override
    public ProfileResponse getAccountProfile(String userId) throws AppCheckedException {
        try {
            ApiResponse<ProfileResponse> profileResponse = profileClient.getProfile(userId);
            return profileResponse.getData();
        } catch (Exception e) {
            throw new AppCheckedException(e.getMessage(), StatusCode.UNCATEGORIZED_EXCEPTION);
        }
    }
}
