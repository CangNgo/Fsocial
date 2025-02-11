package com.fsocial.accountservice.mapper;

import com.fsocial.accountservice.dto.request.account.AccountRegisterRequest;
import com.fsocial.accountservice.dto.request.ProfileRegisterRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    ProfileRegisterRequest toProfileRegister(AccountRegisterRequest request);
}
