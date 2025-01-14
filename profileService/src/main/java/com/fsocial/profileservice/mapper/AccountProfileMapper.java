package com.fsocial.profileservice.mapper;

import com.fsocial.profileservice.dto.request.ProfileRegisterRequest;
import com.fsocial.profileservice.dto.request.ProfileUpdateRequest;
import com.fsocial.profileservice.dto.response.ProfileResponse;
import com.fsocial.profileservice.dto.response.ProfileUpdateResponse;
import com.fsocial.profileservice.entity.AccountProfile;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AccountProfileMapper {
    AccountProfile toAccountProfile(ProfileRegisterRequest request);

    ProfileResponse toProfileResponse(AccountProfile accountProfile);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toAccountProfile(ProfileUpdateRequest request, @MappingTarget AccountProfile accountProfile);

    ProfileUpdateResponse toProfileUpdateResponse(AccountProfile accountProfile);
}
