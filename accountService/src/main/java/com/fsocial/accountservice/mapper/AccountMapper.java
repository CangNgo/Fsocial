package com.fsocial.accountservice.mapper;

import com.fsocial.accountservice.dto.AccountDTO;
import com.fsocial.accountservice.entity.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    Account toEntity(AccountDTO accountDTO);
    AccountDTO toAccountDTO(Account account);
}
