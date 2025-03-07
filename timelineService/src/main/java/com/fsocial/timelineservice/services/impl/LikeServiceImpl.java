package com.fsocial.timelineservice.services.impl;

import com.fsocial.timelineservice.repository.LikeRepository;
import com.fsocial.timelineservice.services.LikeService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LikeServiceImpl implements LikeService {
    LikeRepository likeRepository;
    @Override
    public boolean isLikeByPostIdAndUserId(String postId, String userId) {
        return likeRepository.existsByPostIdAndUserIds(postId,userId);
    }
}
