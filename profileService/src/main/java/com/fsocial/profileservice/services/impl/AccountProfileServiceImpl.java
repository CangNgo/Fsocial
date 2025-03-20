package com.fsocial.profileservice.services.impl;

import com.fsocial.profileservice.dto.request.ProfileRegisterRequest;
import com.fsocial.profileservice.dto.request.ProfileUpdateRequest;
import com.fsocial.profileservice.dto.response.*;
import com.fsocial.profileservice.entity.AccountProfile;
import com.fsocial.profileservice.enums.ProfileVisibility;
import com.fsocial.profileservice.exception.AppException;
import com.fsocial.profileservice.enums.ErrorCode;
import com.fsocial.profileservice.mapper.AccountProfileMapper;
import com.fsocial.profileservice.repository.AccountProfileRepository;
import com.fsocial.profileservice.services.AccountProfileService;
import com.fsocial.profileservice.services.FollowService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AccountProfileServiceImpl implements AccountProfileService {
    FollowService followService;
    AccountProfileRepository accountProfileRepository;
    AccountProfileMapper accountProfileMapper;

    @Override
    @Transactional
    public ProfileResponse createAccountProfile(ProfileRegisterRequest request) {
        AccountProfile accountProfile = accountProfileMapper.toAccountProfile(request);
        accountProfileRepository.save(accountProfile);
        log.info("Tạo thành công hồ sơ cho người dùng: {}", accountProfile.getUserId());
        return accountProfileMapper.toProfileResponse(accountProfile);
    }

    @Override
    public ProfileResponse getAccountProfileByUserId(String userId) {
        return accountProfileMapper.toProfileResponse(findProfileByUserId(userId));
    }

    @Override
    @Transactional
    public ProfileUpdateResponse updateProfile(String userId, ProfileUpdateRequest request) {
        AccountProfile accountProfile = findProfileByUserId(userId);

        accountProfileMapper.toAccountProfile(request, accountProfile);
        accountProfile.setUpdatedAt(LocalDate.now());
        accountProfileRepository.save(accountProfile);
        log.info("Cập nhật hồ sơ thành công cho userId: {}", userId);

        return accountProfileMapper.toProfileUpdateResponse(accountProfile);
    }

    @Override
    public ProfileNameResponse getProfileNameByUserId(String userId) {
        AccountProfile accountProfile = findProfileByUserId(userId);
        return new ProfileNameResponse(accountProfile.getFirstName(), accountProfile.getLastName());
    }

    @Override
    public ProfilePageResponse getProfilePageByUserId(String userId) {
        List<UserResponse> followers = followService.getFollowers(userId);
        var response = accountProfileMapper.toProfilePageResponse(findProfileByUserId(userId));
        response.setFollowers(followers);
        return response;
    }

    @Override
    public ProfilePageOtherResponse getProfilePageOther(String ownerId, String userId) {
        ProfilePageResponse response = getProfilePageByUserId(userId);
        boolean relationship = followService.isFollowing(ownerId, userId);

        ProfilePageOtherResponse result = accountProfileMapper.toProfilePageOtherResponse(response);
        result.setRelationship(relationship);

        return result;
    }

    private AccountProfile findProfileByUserId(String userId) {
        return accountProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.PROFILE_NOT_EXISTED));
    }

    @Override
    public void updateProfileVisibility(String userId, boolean visibility) {
        Optional<AccountProfile> optionalProfile = accountProfileRepository.findByUserId(userId);

        if (optionalProfile.isPresent()) {
            AccountProfile profile = optionalProfile.get();

            // Cập nhật profileVisibility với giá trị boolean
            profile.setProfileVisibility(visibility);
            accountProfileRepository.save(profile);
        } else {
            throw new RuntimeException("Profile not found for user: " + userId);
        }
    }
}
