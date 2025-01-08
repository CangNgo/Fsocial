package com.fsocial.accountservice.services;

import com.fsocial.accountservice.dto.request.AccountRequest;
import com.fsocial.accountservice.dto.response.AccountResponse;

public interface AccountService {
    AccountResponse registerUser(AccountRequest request);
    AccountResponse getUser(String id);
}
