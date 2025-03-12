package com.fsocial.relationshipService.service;

import java.util.Set;

public interface RelationshipService {
    void follow(String userId, String targetId);
    void unfollow(String userId, String targetId);
    boolean isFollowing(String userId, String targetId);
    Set<String> getFollowers(String userId);
    Set<String> getFollowing(String userId);
}
