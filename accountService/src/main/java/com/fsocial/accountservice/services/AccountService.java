package com.fsocial.accountservice.services;

import com.fsocial.accountservice.dto.request.account.AccountRegisterRequest;
import com.fsocial.accountservice.dto.request.account.DuplicationRequest;
import com.fsocial.accountservice.dto.response.AccountResponse;

public interface AccountService {
    void persistAccount(AccountRegisterRequest request);
    AccountResponse getUser(String id);
    void resetPassword(String email, String otp, String newPassword);
    void checkDuplication(DuplicationRequest request);
}
