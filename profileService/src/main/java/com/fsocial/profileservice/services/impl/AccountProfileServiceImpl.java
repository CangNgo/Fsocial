package com.fsocial.profileservice.services.impl;

import com.fsocial.profileservice.dto.request.AccountProfileRequest;
import com.fsocial.profileservice.dto.response.AccountProfileResponse;
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
    public AccountProfileResponse createAccountProfile(AccountProfileRequest request) {
        AccountProfile accountProfile = accountProfileRepository.save(accountProfileMapper.toAccountProfile(request));
        return accountProfileMapper.toProfileResponse(accountProfile);
    }

    @Override
    public AccountProfileResponse getAccountProfile(String id) {
        AccountProfile accountProfile = accountProfileRepository.findById(id).orElseThrow(() -> new AppException(StatusCode.UNCATEGORIZED_EXCEPTION));

        return accountProfileMapper.toProfileResponse(accountProfile);
    }
}
