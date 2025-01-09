package com.fsocial.accountservice.services.impl;

import com.fsocial.accountservice.dto.AccountDTO;
import com.fsocial.accountservice.entity.Account;
import com.fsocial.accountservice.exception.AppCheckedException;
import com.fsocial.accountservice.exception.StatusCode;
import com.fsocial.accountservice.mapper.AccountMapper;
import com.fsocial.accountservice.repository.AccountRepository;
import com.fsocial.accountservice.services.AccountServices;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class AccountServcies implements AccountServices {

    AccountRepository accountRepository;
    AccountMapper accountMapper;

    @Transactional
    public AccountDTO registerUser(AccountDTO accountDTO) throws AppCheckedException {

        try {

            Account result = accountMapper.toEntity(accountDTO);


            result.setCreatedAt(LocalDateTime.now());
            result.setCreatedBy(UUID.randomUUID());

            result = accountRepository.save(result);

            return accountMapper.toAccountDTO(result);
        } catch (DataIntegrityViolationException e) {
            throw new AppCheckedException("Thêm tài khoản mới thất bại", StatusCode.REGISTER_FAILED);
        }

    }
}
