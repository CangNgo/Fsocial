package com.fsocial.profileservice.services.impl;

import com.fsocial.profileservice.dto.request.ProfileRegisterRequest;
import com.fsocial.profileservice.dto.request.ProfileUpdateRequest;
import com.fsocial.profileservice.dto.response.ProfileNameResponse;
import com.fsocial.profileservice.dto.response.ProfilePageResponse;
import com.fsocial.profileservice.dto.response.ProfileResponse;
import com.fsocial.profileservice.dto.response.ProfileUpdateResponse;
import com.fsocial.profileservice.entity.AccountProfile;
import com.fsocial.profileservice.exception.AppException;
import com.fsocial.profileservice.enums.ErrorCode;
import com.fsocial.profileservice.mapper.AccountProfileMapper;
import com.fsocial.profileservice.repository.AccountProfileRepository;
import com.fsocial.profileservice.services.AccountProfileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AccountProfileServiceImpl implements AccountProfileService {

    AccountProfileRepository accountProfileRepository;
    AccountProfileMapper accountProfileMapper;

    @Override
    @Transactional
    public ProfileResponse createAccountProfile(ProfileRegisterRequest request) {
        AccountProfile accountProfile = accountProfileMapper.toAccountProfile(request);
        accountProfileRepository.save(accountProfile);
        log.info("Profile created for user: {}", accountProfile.getUserId());
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
        log.info("Profile updated for user: {}", userId);

        return accountProfileMapper.toProfileUpdateResponse(accountProfile);
    }

    @Override
    public ProfileNameResponse getProfileNameByUserId(String userId) {
        AccountProfile accountProfile = findProfileByUserId(userId);
        return new ProfileNameResponse(accountProfile.getFirstName(), accountProfile.getLastName());
    }

    @Override
    public ProfilePageResponse getProfilePageByUserId(String userId) {
        return accountProfileMapper.toProfilePageResponse(findProfileByUserId(userId));
    }

    private AccountProfile findProfileByUserId(String userId) {
        return accountProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.PROFILE_NOT_EXISTED));
    }
}
