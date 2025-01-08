package com.fsocial.accountservice.services.impl;

import com.fsocial.accountservice.dto.request.AccountRequest;
import com.fsocial.accountservice.dto.response.AccountResponse;
import com.fsocial.accountservice.entity.Account;
import com.fsocial.accountservice.entity.Role;
import com.fsocial.accountservice.exception.AppException;
import com.fsocial.accountservice.exception.StatusCode;
import com.fsocial.accountservice.mapper.AccountMapper;
import com.fsocial.accountservice.repository.AccountRepository;
import com.fsocial.accountservice.repository.RoleRepository;
import com.fsocial.accountservice.services.AccountService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class AccountServiceImpl implements AccountService {

    AccountRepository accountRepository;
    RoleRepository roleRepository;

    AccountMapper accountMapper;

    PasswordEncoder passwordEncoder;

    @Override
    public AccountResponse registerUser(AccountRequest request){

        if (accountRepository.existsByUsername(request.getUsername()))
            throw new AppException(StatusCode.ACCOUNT_EXISTED);

        return accountMapper.toAccountResponse(accountRepository.save(handleAccountAfterSave(request)));
    }

    @Override
    public AccountResponse getUser(String id) {
        Account account = accountRepository.findById(id).orElseThrow(
                () -> new AppException(StatusCode.NOT_EXIST)
        );

        return accountMapper.toAccountResponse(account);
    }

    private Account handleAccountAfterSave(AccountRequest request) {
        Account account = accountMapper.toEntity(request);
        account.setCreatedAt(LocalDateTime.now());
        account.setPassword(passwordEncoder.encode(request.getPassword()));
        account.setRole(getDefaultRole());

        return account;
    }

    private Role getDefaultRole() {
        return roleRepository.findById("USER").orElseThrow();
    }
}
