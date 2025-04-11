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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/follow")
public class FollowController {
    FollowService followService;

    @GetMapping("/{userId}")
    public ApiResponse<Void> followUser(@PathVariable String userId) {
        String ownerId = SecurityContextHolder.getContext().getAuthentication().getName();
        followService.followUser(ownerId,userId);
        return ApiResponse.buildApiResponse(null, ResponseStatus.SUCCESS);
    }

    @DeleteMapping("/{userId}")
    public ApiResponse<String> unfollowUser(@PathVariable String userId) {
        String ownerId = SecurityContextHolder.getContext().getAuthentication().getName();
        followService.unfollowUser(ownerId, userId);
        return ApiResponse.buildApiResponse(null, ResponseStatus.SUCCESS);
    }

    @GetMapping("/is-following/{userId}")
    public ApiResponse<Boolean> isFollowing(@PathVariable String userId) {
        String ownerId = SecurityContextHolder.getContext().getAuthentication().getName();
        Boolean response = followService.isFollowing(ownerId, userId);
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

    @GetMapping("/list_following")
    public ApiResponse<Map<String, List<String>>> getFollowingOfFollower() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        List<String> response = followService.getListFollowers(userId);
        Map<String, List<String>> res = new HashMap<>();
        res.put("listFollowing", response);
        return ApiResponse.buildApiResponse(res, ResponseStatus.SUCCESS);
    }
}
