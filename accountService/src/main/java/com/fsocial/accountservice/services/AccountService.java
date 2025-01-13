package com.fsocial.accountservice.services;

import com.fsocial.accountservice.dto.request.account.AccountRegisterRequest;
import com.fsocial.accountservice.dto.response.AccountResponse;

public interface AccountService {
    AccountResponse registerUser(AccountRegisterRequest request);
    AccountResponse getUser(String id);
}
