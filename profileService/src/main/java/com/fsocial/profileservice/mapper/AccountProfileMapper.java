package com.fsocial.profileservice.mapper;

import com.fsocial.profileservice.dto.request.ProfileRegisterRequest;
import com.fsocial.profileservice.dto.request.ProfileUpdateRequest;
import com.fsocial.profileservice.dto.response.*;
import com.fsocial.profileservice.entity.AccountProfile;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AccountProfileMapper {
    AccountProfile toAccountProfile(ProfileRegisterRequest request);

    ProfileResponse toProfileResponse(AccountProfile accountProfile);

//    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
//    void toAccountProfile(ProfileUpdateRequest request, @MappingTarget AccountProfile accountProfile);

    ProfileUpdateResponse toProfileUpdateResponse(AccountProfile accountProfile);
    ProfilePageResponse toProfilePageResponse(AccountProfile entity);
    ProfileAdminResponse toProfileAdminResponse(AccountProfile accountProfile);
    UserResponse toUserResponse(AccountProfile accountProfile);
    ProfilePageOtherResponse toProfilePageOtherResponse(ProfilePageResponse request);
}
