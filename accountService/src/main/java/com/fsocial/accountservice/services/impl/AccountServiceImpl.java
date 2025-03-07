package com.fsocial.accountservice.services.impl;

import com.fsocial.accountservice.dto.ApiResponse;
import com.fsocial.accountservice.dto.request.account.AccountRegisterRequest;
import com.fsocial.accountservice.dto.request.account.DuplicationRequest;
import com.fsocial.accountservice.dto.response.AccountResponse;
import com.fsocial.accountservice.dto.response.auth.DuplicationResponse;
import com.fsocial.accountservice.entity.Account;
import com.fsocial.accountservice.entity.Role;
import com.fsocial.accountservice.enums.RedisKeyType;
import com.fsocial.accountservice.exception.AppException;
import com.fsocial.accountservice.enums.ErrorCode;
import com.fsocial.accountservice.enums.ResponseStatus;
import com.fsocial.accountservice.mapper.AccountMapper;
import com.fsocial.accountservice.mapper.ProfileMapper;
import com.fsocial.accountservice.repository.AccountRepository;
import com.fsocial.accountservice.repository.RoleRepository;
import com.fsocial.accountservice.repository.httpclient.ProfileClient;
import com.fsocial.accountservice.services.AccountService;
import com.fsocial.accountservice.services.OtpService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
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
    OtpService otpService;

    static String DEFAULT_ROLE = "USER";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void persistAccount(AccountRegisterRequest request) {
        validateAccountExistence(request.getUsername(), request.getEmail());
        Account account = saveAccount(request);
        createProfile(account, request);
        otpService.deleteOtp(request.getEmail(), RedisKeyType.REGISTER.getRedisKeyPrefix());
    }

    @Override
    public AccountResponse getUser(String id) {
        return accountRepository.findById(id)
                .map(accountMapper::toAccountResponse)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED));
    }

    @Override
    public void resetPassword(String email, String newPassword) {
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED));

        account.setPassword(passwordEncoder.encode(newPassword));
        account.setUpdatedAt(LocalDateTime.now());
        account.setUpdatedBy(account.getId());
        accountRepository.save(account);
        log.info("Đặt lại mật khẩu thành công.");
    }

    @Override
    public ApiResponse<DuplicationResponse> checkDuplication(DuplicationRequest request) {
        boolean usernameExisted = accountRepository.countByUsername(request.getUsername()) > 0;
        boolean emailExisted = accountRepository.countByEmail(request.getEmail()) > 0;

        DuplicationResponse response = DuplicationResponse.builder()
                .username(usernameExisted ? ErrorCode.USERNAME_EXISTED.getMessage() : null)
                .email(emailExisted ? ErrorCode.EMAIL_EXISTED.getMessage() : null)
                .build();

        boolean hasError = usernameExisted || emailExisted;

        return ApiResponse.<DuplicationResponse>builder()
                .statusCode(hasError ? ErrorCode.DUPLICATION.getCode() : ResponseStatus.VALID.getCODE())
                .message(hasError ? ErrorCode.DUPLICATION.getMessage() : ResponseStatus.VALID.getMessage())
                .data(hasError ? response : null)
                .build();
    }

    @Override
    public void changePassword(String userId, String odlPassword, String newPassword) {
        Account account = accountRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED));

        if (!passwordEncoder.matches(odlPassword, account.getPassword())) {
            throw new AppException(ErrorCode.WRONG_PASSWORD);
        }

        account.setPassword(passwordEncoder.encode(newPassword));
        account.setUpdatedAt(LocalDateTime.now());
        account.setUpdatedBy(account.getId());
        accountRepository.save(account);
        log.info("Đổi mật khẩu thành công.");
    }

    private void validateAccountExistence(String username, String email) {
        boolean accountExisted = accountRepository.countByUsernameOrEmail(username, email) > 0;
        if (accountExisted) throw new AppException(ErrorCode.ACCOUNT_EXISTED);

        otpService.validEmailBeforePersist(email);
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
        return roleRepository.findById(DEFAULT_ROLE)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
    }
}
