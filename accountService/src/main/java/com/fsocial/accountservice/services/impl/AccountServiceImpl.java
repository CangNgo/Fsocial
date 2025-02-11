package com.fsocial.accountservice.services.impl;

import com.fsocial.accountservice.dto.request.account.AccountRegisterRequest;
import com.fsocial.accountservice.dto.request.account.DuplicationRequest;
import com.fsocial.accountservice.dto.response.AccountResponse;
import com.fsocial.accountservice.dto.response.ProfileRegisterResponse;
import com.fsocial.accountservice.entity.Account;
import com.fsocial.accountservice.entity.Role;
import com.fsocial.accountservice.exception.AppException;
import com.fsocial.accountservice.enums.StatusCode;
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
    public void persistAccount(AccountRegisterRequest request) {
        validateAccountExistence(request.getUsername(), request.getEmail());
        Account account = saveAccount(request);
        buildAccountResponse(account, createProfile(account, request));
    }

    @Override
    public AccountResponse getUser(String id) {
        return accountRepository.findById(id)
                .map(accountMapper::toAccountResponse)
                .orElseThrow(() -> new AppException(StatusCode.NOT_EXIST));
    }

    @Override
    public void resetPassword(String email, String otp, String newPassword) {
        otpService.validateOtp(email, otp, "RESET_");

        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(StatusCode.NOT_EXIST));

        account.setPassword(passwordEncoder.encode(newPassword));
        accountRepository.save(account);
    }

    @Override
    public void checkDuplication(DuplicationRequest request) {
        if (accountRepository.existsByUsername(request.getUsername()))
            throw new AppException(StatusCode.USERNAME_EXISTED);

        if (accountRepository.existsByEmail(request.getEmail()))
            throw new AppException(StatusCode.EMAIL_EXISTED);
    }

    private void validateAccountExistence(String username, String email) {
        if (accountRepository.existsByUsername(username))
            throw new AppException(StatusCode.ACCOUNT_EXISTED);

        if ( accountRepository.existsByEmail(email))
            throw new AppException(StatusCode.EMAIL_EXISTED);
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
                .orElseThrow(() -> new AppException(StatusCode.NOT_FOUND));
    }
}
