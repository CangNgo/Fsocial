package com.fsocial.relationshipService.controller;

import com.fsocial.relationshipService.dto.ApiResponse;
import com.fsocial.relationshipService.dto.request.FollowRequest;
import com.fsocial.relationshipService.enums.ResponseStatus;
import com.fsocial.relationshipService.service.RelationshipService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RelationshipController {
    RelationshipService relationshipService;

    @PostMapping("/follow")
    public ApiResponse<Void> follow(@RequestBody @Valid FollowRequest request) {
        relationshipService.follow(request.getUserId(), request.getTargetId());
        return  buildApiResponse(null);
    }

    @PostMapping("/unfollow")
    public ApiResponse<Void> unfollow(@RequestBody @Valid FollowRequest request) {
        relationshipService.unfollow(request.getUserId(), request.getTargetId());
        return buildApiResponse(null);
    }

    @GetMapping("/followers/{userId}")
    public ApiResponse<Set<String>> getFollowers(@PathVariable String userId) {
        return buildApiResponse(relationshipService.getFollowers(userId));
    }

    @GetMapping("/followings/{userId}")
    public ApiResponse<Set<String>> getFollowings(@PathVariable String userId) {
        return buildApiResponse(relationshipService.getFollowing(userId));
    }

    private <T> ApiResponse<T> buildApiResponse(T data) {
        return ApiResponse.<T>builder()
                .statusCode(ResponseStatus.SUCCESS.getCODE())
                .message(ResponseStatus.SUCCESS.getMessage())
                .data(data)
                .build();
    }
}
