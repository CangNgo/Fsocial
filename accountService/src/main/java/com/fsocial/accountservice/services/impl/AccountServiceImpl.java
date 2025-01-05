package com.fsocial.accountservice.services.impl;

import com.fsocial.accountservice.dto.request.AccountRequest;
import com.fsocial.accountservice.dto.response.AccountResponse;
import com.fsocial.accountservice.entity.Account;
import com.fsocial.accountservice.exception.AppException;
import com.fsocial.accountservice.exception.StatusCode;
import com.fsocial.accountservice.mapper.AccountMapper;
import com.fsocial.accountservice.repository.AccountRepository;
import com.fsocial.accountservice.services.AccountService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class AccountServiceImpl implements AccountService {

    AccountRepository accountRepository;
    AccountMapper accountMapper;

    PasswordEncoder passwordEncoder;

    @Transactional
    public AccountResponse registerUser(AccountRequest request){

        if (accountRepository.existsByUsername(request.getUsername()))
            throw new AppException(StatusCode.ACCOUNT_EXISTED);

        Account account = accountMapper.toEntity(request);
        account.setCreatedAt(LocalDateTime.now());
        account.setPassword(passwordEncoder.encode(request.getPassword()));

        return accountMapper.toAccountResponse(accountRepository.save(account));
    }
}
