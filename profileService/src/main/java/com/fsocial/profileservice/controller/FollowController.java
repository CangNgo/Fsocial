package com.fsocial.profileservice.controller;

import com.fsocial.profileservice.dto.ApiResponse;
import com.fsocial.profileservice.dto.response.UserResponse;
import com.fsocial.profileservice.enums.ResponseStatus;
import com.fsocial.profileservice.services.FollowService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/follow")
public class FollowController {
    FollowService followService;

    @GetMapping("/{userId}")
    public ApiResponse<Void> followUser(@PathVariable String userId) {
        followService.followUser(userId);
        return ApiResponse.buildApiResponse(null, ResponseStatus.SUCCESS);
    }

    @DeleteMapping("/{userId}")
    public ApiResponse<String> unfollowUser(@PathVariable String userId) {
        followService.unfollowUser(userId);
        return ApiResponse.buildApiResponse(null, ResponseStatus.SUCCESS);
    }

    @GetMapping("/is-following/{userId}")
    public ApiResponse<Boolean> isFollowing(@PathVariable String userId) {
        Boolean response = followService.isFollowing(userId);
        return ApiResponse.buildApiResponse(response, ResponseStatus.SUCCESS);
    }

    @GetMapping("/following")
    public ApiResponse<List<UserResponse>> getFollowingOfOwner() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        List<UserResponse> response = followService.getFollowingUsers(userId);
        return ApiResponse.buildApiResponse(response, ResponseStatus.SUCCESS);
    }

    @GetMapping("/followers")
    public ApiResponse<List<UserResponse>> getFollowersOfOwner() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        List<UserResponse> response = followService.getFollowers(userId);
        return ApiResponse.buildApiResponse(response, ResponseStatus.SUCCESS);
    }

    @GetMapping("/following/{userId}")
    public ApiResponse<List<UserResponse>> getFollowingOfAnotherUser(@PathVariable String userId) {
        List<UserResponse> response = followService.getFollowingUsers(userId);
        return ApiResponse.buildApiResponse(response, ResponseStatus.SUCCESS);
    }

    @GetMapping("/followers/{userId}")
    public ApiResponse<List<UserResponse>> getFollowersOfAnotherUser(@PathVariable String userId) {
        List<UserResponse> response = followService.getFollowers(userId);
        return ApiResponse.buildApiResponse(response, ResponseStatus.SUCCESS);
    }
}
