package com.fsocial.accountservice.mapper;

import com.fsocial.accountservice.dto.request.AccountRegisterRequest;
import com.fsocial.accountservice.dto.response.AccountResponse;
import com.fsocial.accountservice.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    Account toEntity(AccountRegisterRequest accountDTO);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "username", source = "username")
    AccountResponse toAccountResponse(Account account);
}
