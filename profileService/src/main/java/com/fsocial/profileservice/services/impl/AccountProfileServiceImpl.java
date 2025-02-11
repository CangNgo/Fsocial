package com.fsocial.profileservice.services.impl;

import com.fsocial.profileservice.dto.request.ProfileRegisterRequest;
import com.fsocial.profileservice.dto.request.ProfileUpdateRequest;
import com.fsocial.profileservice.dto.response.ProfileResponse;
import com.fsocial.profileservice.dto.response.ProfileUpdateResponse;
import com.fsocial.profileservice.entity.AccountProfile;
import com.fsocial.profileservice.exception.AppException;
import com.fsocial.profileservice.exception.StatusCode;
import com.fsocial.profileservice.mapper.AccountProfileMapper;
import com.fsocial.profileservice.repository.AccountProfileRepository;
import com.fsocial.profileservice.services.AccountProfileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountProfileServiceImpl implements AccountProfileService {

    AccountProfileRepository accountProfileRepository;
    AccountProfileMapper accountProfileMapper;

    @Override
    public ProfileResponse createAccountProfile(ProfileRegisterRequest request) {
        AccountProfile accountProfile = accountProfileRepository.save(accountProfileMapper.toAccountProfile(request));
        return accountProfileMapper.toProfileResponse(accountProfile);
    }

    @Override
    public ProfileResponse getAccountProfile(String userId) {

        return accountProfileRepository.findByUserId(userId).orElseThrow(
                () -> new AppException(StatusCode.UNCATEGORIZED_EXCEPTION)
        );
    }

    @Override
    public ProfileUpdateResponse updateProfile(String profileId, ProfileUpdateRequest request) {
        if (!accountProfileRepository.existsById(profileId))
            throw new AppException(StatusCode.NOT_EXISTED);

        AccountProfile accountProfile = accountProfileRepository.findById(profileId).orElseThrow(
                () -> new AppException(StatusCode.UNCATEGORIZED_EXCEPTION)
        );

        accountProfileMapper.toAccountProfile(request, accountProfile);

        return accountProfileMapper.toProfileUpdateResponse(
                accountProfileRepository.save(accountProfile)
        );
    }

}
