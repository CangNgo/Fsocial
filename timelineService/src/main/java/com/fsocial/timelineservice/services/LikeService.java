package com.fsocial.timelineservice.services;

public interface LikeService {
    boolean isLikeByPostIdAndUserId(String postId, String userId);
}
