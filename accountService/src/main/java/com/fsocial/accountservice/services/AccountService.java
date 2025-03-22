package com.fsocial.accountservice.services;

import com.fsocial.accountservice.dto.ApiResponse;
import com.fsocial.accountservice.dto.request.account.AccountRegisterRequest;
import com.fsocial.accountservice.dto.request.account.DuplicationRequest;
import com.fsocial.accountservice.dto.response.AccountResponse;
import com.fsocial.accountservice.dto.response.AccountStatisticRegiserDTO;
import com.fsocial.accountservice.dto.response.auth.DuplicationResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface AccountService {
    void persistAccount(AccountRegisterRequest request);
    AccountResponse getUser(String id);
    void resetPassword(String email, String newPassword);
    ApiResponse<DuplicationResponse> checkDuplication(DuplicationRequest request);
    void changePassword(String userId, String odlPassword, String newPassword);
    boolean existsById(String id);
    List<AccountStatisticRegiserDTO> countByCreatedAtByHours(LocalDateTime startDay, LocalDateTime endDay);
}
