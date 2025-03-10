package com.fsocial.profileservice.services.impl;

import com.fsocial.profileservice.dto.response.UserResponse;
import com.fsocial.profileservice.enums.ErrorCode;
import com.fsocial.profileservice.exception.AppException;
import com.fsocial.profileservice.mapper.AccountProfileMapper;
import com.fsocial.profileservice.repository.AccountProfileRepository;
import com.fsocial.profileservice.services.FollowService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class FollowServiceImpl implements FollowService {
    AccountProfileMapper accountProfileMapper;
    AccountProfileRepository accountProfileRepository;

    @Override
    @Transactional(rollbackFor = AppException.class)
    public void followUser(String userId) {
        validUserId(userId);
        // Lấy userId của chính chủ từ Token
        String ownerId = SecurityContextHolder.getContext().getAuthentication().getName();
        accountProfileRepository.followUser(ownerId, userId);
    }

    @Override
    @Transactional
    public void unfollowUser(String userId) {
        validUserId(userId);
        // Lấy userId của chính chủ từ Token
        String ownerId = SecurityContextHolder.getContext().getAuthentication().getName();

        accountProfileRepository.unfollowUser(ownerId, userId);
    }

    @Override
    public boolean isFollowing(String userId) {
        validUserId(userId);
        // Lấy userId của chính chủ từ Token
        String ownerId = SecurityContextHolder.getContext().getAuthentication().getName();
        return accountProfileRepository.getFollowingUsers(ownerId)
                .stream()
                .anyMatch(user -> user.getUserId().equals(userId));
    }

    @Override
    public List<UserResponse> getFollowingUsers(String userId) {
        return accountProfileRepository.getFollowingUsers(userId)
                .stream()
                .map(accountProfileMapper::toUserResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserResponse> getFollowers(String userId) {
        return accountProfileRepository.getFollowers(userId)
                .stream()
                .map(accountProfileMapper::toUserResponse)
                .toList();
    }

    private void validUserId(String userId) {
        if (!accountProfileRepository.existsByUserId(userId)) {
            log.warn("Tài khoản không tồn tại.");
            throw new AppException(ErrorCode.ACCOUNT_EXISTED);
        }
    }
}
