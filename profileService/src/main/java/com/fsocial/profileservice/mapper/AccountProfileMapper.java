package com.fsocial.profileservice.mapper;

import com.fsocial.profileservice.dto.request.AccountProfileRequest;
import com.fsocial.profileservice.dto.response.AccountProfileResponse;
import com.fsocial.profileservice.entity.AccountProfile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountProfileMapper {
    AccountProfile toAccountProfile(AccountProfileRequest request);
    AccountProfileResponse toProfileResponse(AccountProfile accountProfile);
}
