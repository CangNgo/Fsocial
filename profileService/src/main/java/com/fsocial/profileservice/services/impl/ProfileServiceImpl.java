package com.fsocial.profileservice.services.impl;

import com.fsocial.profileservice.dto.response.FindProfileDTO;
import com.fsocial.profileservice.entity.AccountProfile;
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

    @Override
    public List<FindProfileDTO> findByCombinedName(String combinedName) {
        return profileRepository.findByFirstName(combinedName);
    }
}
