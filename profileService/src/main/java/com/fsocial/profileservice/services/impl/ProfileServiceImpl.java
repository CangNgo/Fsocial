package com.fsocial.profileservice.services.impl;

import com.fsocial.profileservice.dto.response.FindProfileDTO;
import com.fsocial.profileservice.dto.response.ProfileAdminResponse;
import com.fsocial.profileservice.entity.AccountProfile;
import com.fsocial.profileservice.enums.ErrorCode;
import com.fsocial.profileservice.exception.AppCheckedException;
import com.fsocial.profileservice.mapper.AccountProfileMapper;
import com.fsocial.profileservice.repository.ProfileRepository;
import com.fsocial.profileservice.services.ProfileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProfileServiceImpl implements ProfileService {

    ProfileRepository profileRepository;
    AccountProfileMapper accountProfileMapper;

    @Override
    public List<FindProfileDTO> findByCombinedName(String combinedName) {
        return profileRepository.findByFirstName(combinedName);
    }

    @Override
    public ProfileAdminResponse getProfileAdmin(String profileId) throws AppCheckedException {
        AccountProfile profile = profileRepository.findById( profileId)
                .orElseThrow(()-> new AppCheckedException("Không tìm thấy thông tin người dùng", ErrorCode.NOT_FOUND));
        return getProfileAdminResponse(profile);
    }

    private ProfileAdminResponse getProfileAdminResponse(AccountProfile accountProfile) {
        return accountProfileMapper.toProfileAdminResponse(accountProfile);
    }
}
