package com.fsocial.profileservice.services;

import com.fsocial.profileservice.dto.request.AccountProfileRequest;
import com.fsocial.profileservice.dto.response.AccountProfileResponse;

public interface AccountProfileService {
    AccountProfileResponse createAccountProfile(AccountProfileRequest request);
    AccountProfileResponse getAccountProfile(String id);
}
