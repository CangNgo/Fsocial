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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class FollowServiceImpl implements FollowService {
    AccountProfileMapper accountProfileMapper;
    AccountProfileRepository accountProfileRepository;
    RedisTemplate<String, Boolean> redisTemplate;

    @Override
    @Transactional(rollbackFor = AppException.class)
    public void followUser(String userId) {
        validUserId(userId);
        // Lấy userId của chính chủ từ Token
        String ownerId = SecurityContextHolder.getContext().getAuthentication().getName();
        checkFollowerBeforeFollowOrUnFollow(ownerId, userId);

        // Kiểm tra nếu đã follow trước đó
        if (accountProfileRepository.isFollowing(ownerId, userId)) {
            log.warn("Người dùng {} đã theo dõi {}", ownerId, userId);
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }

        accountProfileRepository.followUser(ownerId, userId);
    }

    @Override
    @Transactional
    public void unfollowUser(String userId) {
        validUserId(userId);
        // Lấy userId của chính chủ từ Token
        String ownerId = SecurityContextHolder.getContext().getAuthentication().getName();
        checkFollowerBeforeFollowOrUnFollow(ownerId, userId);

        // Kiểm tra nếu chưa follow thì không thể unfollow
        if (!accountProfileRepository.isFollowing(ownerId, userId)) {
            log.warn("Người dùng {} chưa theo dõi {}, không thể bỏ theo dõi", ownerId, userId);
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }

        accountProfileRepository.unfollowUser(ownerId, userId);
    }

    @Override
    public boolean isFollowing(String userId) {
        validUserId(userId);
        // Lấy userId của chính chủ từ Token
        String ownerId = SecurityContextHolder.getContext().getAuthentication().getName();
        checkFollowerBeforeFollowOrUnFollow(ownerId, userId);
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
        String redisKey = "user:exists:" + userId;

        Boolean exists = redisTemplate.opsForValue().get(redisKey);
        if (Boolean.TRUE.equals(exists)) return;

        boolean userExists = accountProfileRepository.existsByUserId(userId);
        if (!userExists) {
            log.warn("Tài khoản {} không tồn tại.", userId);
            throw new AppException(ErrorCode.ACCOUNT_EXISTED);
        }

        // Lưu vào Redis với thời gian hết hạn là 5 phút
        redisTemplate.opsForValue().set(redisKey, true, 5, TimeUnit.MINUTES);
    }

    private void checkFollowerBeforeFollowOrUnFollow(String userId, String followingId) {
        if (userId.equals(followingId)) {
            log.warn("Người dùng không thể tự thao tác trên chính mình.");
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }
}
