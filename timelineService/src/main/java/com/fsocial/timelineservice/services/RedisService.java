package com.fsocial.timelineservice.services;

import com.fsocial.timelineservice.dto.post.PostResponse;
import com.fsocial.timelineservice.entity.Post;

import java.util.List;

public interface RedisService {
    void saveData(String key, String value);

    String getData(String key);

    void saveList(String key, String value);

    List<String> getList(String key);

    void personalization(String userId, String value);

    List<String> getPersonalization(String userId);

    void viewed(String userId, String value);

    List<String> getViewed(String userId);

    void cleaerViewed(String userId);

    void viewedFollowing(String userId, String postId);

    List<String> getViewedFollowing(String userId);

    void clearViewedFollowing(String userId);
}
