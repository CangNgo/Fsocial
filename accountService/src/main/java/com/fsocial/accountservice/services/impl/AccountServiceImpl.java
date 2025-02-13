package com.fsocial.accountservice.services.impl;

import com.fsocial.accountservice.dto.ApiResponse;
import com.fsocial.accountservice.dto.request.account.AccountRegisterRequest;
import com.fsocial.accountservice.dto.request.account.DuplicationRequest;
import com.fsocial.accountservice.dto.response.AccountResponse;
import com.fsocial.accountservice.dto.response.DuplicationResponse;
import com.fsocial.accountservice.entity.Account;
import com.fsocial.accountservice.entity.Role;
import com.fsocial.accountservice.exception.AppException;
import com.fsocial.accountservice.enums.ErrorCode;
import com.fsocial.accountservice.enums.ResponseStatus;
import com.fsocial.accountservice.mapper.AccountMapper;
import com.fsocial.accountservice.mapper.ProfileMapper;
import com.fsocial.accountservice.repository.AccountRepository;
import com.fsocial.accountservice.repository.RoleRepository;
import com.fsocial.accountservice.repository.httpclient.ProfileClient;
import com.fsocial.accountservice.services.AccountService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
@Slf4j
public class AccountServiceImpl implements AccountService {

    AccountRepository accountRepository;
    RoleRepository roleRepository;
    AccountMapper accountMapper;
    ProfileMapper profileMapper;
    PasswordEncoder passwordEncoder;
    ProfileClient profileClient;
    OtpServiceImpl otpService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void persistAccount(AccountRegisterRequest request) {
        validateAccountExistence(request.getUsername(), request.getEmail());
        Account account = saveAccount(request);
        createProfile(account, request);
    }

    @Override
    public AccountResponse getUser(String id) {
        return accountRepository.findById(id)
                .map(accountMapper::toAccountResponse)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED));
    }

    @Override
    public void resetPassword(String email, String otp, String newPassword) {
        try {
            otpService.validateOtp(email, otp, "RESET_");

            Account account = accountRepository.findByEmail(email)
                    .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED));

            account.setPassword(passwordEncoder.encode(newPassword));
            accountRepository.save(account);
        } catch (Exception e) {
            log.warn("Đặt lại mật khẩu cho email không thành công: {}: {}", email, e.getMessage());
        }
    }

    @Override
    public ApiResponse<DuplicationResponse> checkDuplication(DuplicationRequest request) {
        DuplicationResponse response = DuplicationResponse.builder()
                .username(accountRepository.existsByUsername(request.getUsername()) ? ErrorCode.USERNAME_EXISTED.getMessage() : ErrorCode.OK.getMessage())
                .email(accountRepository.existsByEmail(request.getEmail()) ? ErrorCode.EMAIL_EXISTED.getMessage() : ErrorCode.OK.getMessage())
                .build();

        boolean hasError = !response.getUsername().equals(ErrorCode.OK.getMessage()) ||
                !response.getEmail().equals(ErrorCode.OK.getMessage());

        return ApiResponse.<DuplicationResponse>builder()
                .statusCode(hasError ? ErrorCode.DUPLICATION.getCode() : ResponseStatus.VALID.getCODE())
                .message(hasError ? ErrorCode.DUPLICATION.getMessage() : ResponseStatus.VALID.getMessage())
                .data(response)
                .build();
    }

    public ApiResponse<DuplicationResponse> checkDuplications(DuplicationRequest request) {
        DuplicationResponse response = DuplicationResponse.builder()
                .username(accountRepository.existsByUsername(request.getUsername()) ? ErrorCode.USERNAME_EXISTED.getMessage() : ErrorCode.OK.getMessage())
                .email(accountRepository.existsByEmail(request.getEmail()) ? ErrorCode.EMAIL_EXISTED.getMessage() : ErrorCode.OK.getMessage())
                .build();

        boolean hasError = !response.getUsername().equals(ErrorCode.OK.getMessage()) ||
                !response.getEmail().equals(ErrorCode.OK.getMessage());

        return ApiResponse.<DuplicationResponse>builder()
                .statusCode(hasError ? ErrorCode.DUPLICATION.getCode() : ResponseStatus.VALID.getCODE())
                .message(hasError ? ErrorCode.DUPLICATION.getMessage() : ResponseStatus.VALID.getMessage())
                .data(response)
                .build();
    }

    private void validateAccountExistence(String username, String email) {
        boolean accountExisted = accountRepository.countByUsernameOrEmail(username, email) > 0;
        if (accountExisted) {
            throw new AppException(ErrorCode.ACCOUNT_EXISTED);
        }
    }

    private Account saveAccount(AccountRegisterRequest request) {
        Account account = accountMapper.toEntity(request);
        account.setCreatedAt(LocalDateTime.now());
        account.setPassword(passwordEncoder.encode(request.getPassword()));
        account.setRole(getDefaultRole());
        return accountRepository.save(account);
    }

    private void createProfile(Account account, AccountRegisterRequest request) {
        var profileRequest = profileMapper.toProfileRegister(request);
        profileRequest.setUserId(account.getId());
        profileClient.createProfile(profileRequest);
    }

    private Role getDefaultRole() {
        return roleRepository.findById("USER")
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
    }
}
