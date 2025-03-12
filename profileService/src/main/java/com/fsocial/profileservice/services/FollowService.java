package com.fsocial.profileservice.services;

import com.fsocial.profileservice.dto.response.UserResponse;

import java.util.List;

public interface FollowService {
    void followUser(String ownerId, String userId);
    void unfollowUser(String ownerId, String userId);
    boolean isFollowing(String userId, String follower);
    List<UserResponse> getFollowingUsers(String userId);
    List<UserResponse> getFollowers(String userId);
}
