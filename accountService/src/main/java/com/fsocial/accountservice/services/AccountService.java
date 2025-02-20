package com.fsocial.accountservice.services;

import com.fsocial.accountservice.dto.ApiResponse;
import com.fsocial.accountservice.dto.request.account.AccountRegisterRequest;
import com.fsocial.accountservice.dto.request.account.DuplicationRequest;
import com.fsocial.accountservice.dto.response.AccountResponse;
import com.fsocial.accountservice.dto.response.DuplicationResponse;

public interface AccountService {
    void persistAccount(AccountRegisterRequest request);
    AccountResponse getUser(String id);
    void resetPassword(String email, String newPassword);
    ApiResponse<DuplicationResponse> checkDuplication(DuplicationRequest request);
}
