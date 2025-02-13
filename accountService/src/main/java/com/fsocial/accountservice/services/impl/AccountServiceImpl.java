package com.fsocial.accountservice.services.impl;

import com.fsocial.accountservice.dto.request.account.AccountRegisterRequest;
import com.fsocial.accountservice.dto.request.account.DuplicationRequest;
import com.fsocial.accountservice.dto.response.AccountResponse;
import com.fsocial.accountservice.dto.response.DuplicationResponse;
import com.fsocial.accountservice.dto.response.ProfileRegisterResponse;
import com.fsocial.accountservice.entity.Account;
import com.fsocial.accountservice.entity.Role;
import com.fsocial.accountservice.exception.AppException;
import com.fsocial.accountservice.enums.ErrorCode;
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
        try {
            validateAccountExistence(request.getUsername(), request.getEmail());
            Account account = saveAccount(request);
            buildAccountResponse(account, createProfile(account, request));
        } catch (Exception e) {
            log.warn("Lỗi khi đăng ký tài khoản: {}", e.getMessage(), e);
            throw new AppException(ErrorCode.REGISTER_FAILED);
        }
    }

    @Override
    public AccountResponse getUser(String id) {
        return accountRepository.findById(id)
                .map(accountMapper::toAccountResponse)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXIST));
    }

    @Override
    public void resetPassword(String email, String otp, String newPassword) {
        otpService.validateOtp(email, otp, "RESET_");

        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXIST));

        account.setPassword(passwordEncoder.encode(newPassword));
        accountRepository.save(account);
    }

    @Override
    public DuplicationResponse checkDuplication(DuplicationRequest request) {
        boolean usernameExists = accountRepository.existsByUsername(request.getUsername());
        boolean emailExists = accountRepository.existsByEmail(request.getEmail());

        return DuplicationResponse.builder()
                .username(usernameExists ? ErrorCode.USERNAME_EXISTED.getMessage() : ErrorCode.OK.getMessage())
                .email(emailExists ? ErrorCode.EMAIL_EXISTED.getMessage() : ErrorCode.OK.getMessage())
                .build();
    }

    private void validateAccountExistence(String username, String email) {
        if (accountRepository.existsByUsernameOrEmail(username, email)) {
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

    private ProfileRegisterResponse createProfile(Account account, AccountRegisterRequest request) {
        var profileRequest = profileMapper.toProfileRegister(request);
        profileRequest.setUserId(account.getId());
        return profileClient.createProfile(profileRequest);
    }

    private void buildAccountResponse(Account account, ProfileRegisterResponse profileResponse) {
        AccountResponse.builder()
                .id(account.getId())
                .username(account.getUsername())
                .firstName(profileResponse.getFirstName())
                .lastName(profileResponse.getLastName())
                .avatar(profileResponse.getAvatar())
                .build();
    }

    private Role getDefaultRole() {
        return roleRepository.findById("USER")
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
    }
}
