package com.fsocial.profileservice.services;

import com.fsocial.profileservice.dto.response.UserResponse;

import java.util.List;

public interface FollowService {
    void followUser(String userId);
    void unfollowUser(String userId);
    boolean isFollowing(String userId);
    List<UserResponse> getFollowingUsers(String userId);
    List<UserResponse> getFollowers(String userId);
}
