package com.fsocial.profileservice.services.impl;

import com.fsocial.profileservice.dto.request.ProfileRegisterRequest;
import com.fsocial.profileservice.dto.request.ProfileUpdateRequest;
import com.fsocial.profileservice.dto.response.*;
import com.fsocial.profileservice.entity.AccountProfile;
import com.fsocial.profileservice.exception.AppException;
import com.fsocial.profileservice.enums.ErrorCode;
import com.fsocial.profileservice.mapper.AccountProfileMapper;
import com.fsocial.profileservice.repository.AccountProfileRepository;
import com.fsocial.profileservice.repository.httpClient.PostClient;
import com.fsocial.profileservice.services.AccountProfileService;
import com.fsocial.profileservice.services.FollowService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.lang.reflect.Field;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AccountProfileServiceImpl implements AccountProfileService {
    FollowService followService;
    AccountProfileRepository accountProfileRepository;
    AccountProfileMapper accountProfileMapper;
    PostClient postClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProfileResponse createAccountProfile(ProfileRegisterRequest request) {
        AccountProfile accountProfile = accountProfileMapper.toAccountProfile(request);
        accountProfile.setBanner("");
        accountProfile.setBio("");
        accountProfile.setAvatar("");
        accountProfile.setAddress("");
        accountProfile.setUpdatedAt(LocalDate.now());
        accountProfileRepository.save(accountProfile);
        log.info("Tạo thành công hồ sơ cho người dùng: {}", accountProfile.getUserId());
        return accountProfileMapper.toProfileResponse(accountProfile);
    }

    @Override
    public ProfileResponse getAccountProfileByUserId(String userId) {
        return accountProfileMapper.toProfileResponse(findProfileByUserId(userId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProfileUpdateResponse updateProfile(String userId, ProfileUpdateRequest request) {
        AccountProfile accountProfile = findProfileByUserId(userId);
        Map<String, Object> nonNullFields = getNonNullProperties(request);

        nonNullFields.forEach((field, value) -> updateField(accountProfile, field, value));

        accountProfile.setUpdatedAt(LocalDate.now());
        log.info("Cập nhật hồ sơ thành công cho userId: {}", userId);

        return accountProfileMapper.toProfileUpdateResponse(accountProfileRepository.save(accountProfile));
    }

    @Override
    @Transactional(readOnly = true)
    public ProfileNameResponse getProfileNameByUserId(String userId) {
        AccountProfile accountProfile = findProfileByUserId(userId);
        return new ProfileNameResponse(accountProfile.getFirstName(), accountProfile.getLastName());
    }

    @Override
    @Transactional(readOnly = true)
    public ProfilePageResponse getProfilePageByUserId(String userId) {
        List<UserResponse> followers = followService.getFollowers(userId);
        var response = accountProfileMapper.toProfilePageResponse(findProfileByUserId(userId));
        response.setFollowers(followers);
        return response;
    }

    @Override
    public ProfilePageOtherResponse getProfilePageOther(String ownerId, String userId) {
        ProfilePageResponse response = getProfilePageByUserId(userId);
        boolean relationship = followService.isFollowing(ownerId, userId);
        ProfilePageOtherResponse result = accountProfileMapper.toProfilePageOtherResponse(response);
        result.setRelationship(relationship);
        return result;
    }

    @Override
    public UpdatePrivacyResponse updateProfileVisibility(String userId) {
        AccountProfile accProfile = findProfileByUserId(userId);
        accProfile.setPublic(!accProfile.isPublic());
        accountProfileRepository.save(accProfile);
        return UpdatePrivacyResponse.builder()
                .userId(accProfile.getUserId())
                .isPublic(accProfile.isPublic())
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateProfileImage(String userId, MultipartFile file, boolean isAvatar) {
        if (userId == null) throw new AppException(ErrorCode.NOT_NULL);

        AccountProfile profile = findProfileByUserId(userId);
        String urlImage = postClient.uploadFile(file);
        if (Boolean.TRUE.equals(isAvatar))
            profile.setAvatar(urlImage);
        else
            profile.setBanner(urlImage);

        accountProfileRepository.save(profile);
        log.info("Đã cập nhật lại ảnh thành công");
    }

    @Transactional(readOnly = true)
    private AccountProfile findProfileByUserId(String userId) {
        return accountProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.PROFILE_NOT_EXISTED));
    }

    private Map<String, Object> getNonNullProperties(Object object) {
        return java.util.Arrays.stream(object.getClass().getDeclaredFields())
                .peek(field -> field.setAccessible(true))
                .map(field -> {
                    try {
                        return Map.entry(field.getName(), field.get(object));
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("Lỗi khi truy xuất field: " + field.getName(), e);
                    }
                })
                .filter(entry -> entry.getValue() != null)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private void updateField(AccountProfile accountProfile, String field, Object value) {
        try {
            Field entityField = accountProfile.getClass().getDeclaredField(field);
            entityField.setAccessible(true);
            entityField.set(accountProfile, value);
        } catch (NoSuchFieldException e) {
            log.error("Không tìm thấy field {} trong AccountProfile", field);
        } catch (IllegalAccessException e) {
            log.error("Không thể cập nhật field {}: {}", field, e.getMessage());
        }
    }
}