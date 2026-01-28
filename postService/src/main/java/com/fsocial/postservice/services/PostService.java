package com.fsocial.postservice.services;

import com.fsocial.postservice.dto.post.*;
import com.fsocial.postservice.entity.Post;
import com.fsocial.postservice.exception.AppCheckedException;

import java.util.List;

public interface PostService {
    PostDTO createPost(PostDTORequest request) throws AppCheckedException;

    PostDTO updatePost(PostDTORequest post, String postId) throws AppCheckedException;

    void deletePost(String postId);

    boolean toggleLike(String postId, String userId) throws Exception;

    Integer CountLike(String postId, String userId);

    PostDTO sharePost(PostShareDTORequest dto);

    List<Post> getPostsByUser(String userId, String requesterId);

    // Methods from timelineService
    List<PostResponse> getPostsByUserId(String userId) throws AppCheckedException;

    com.fsocial.postservice.dto.profile.ProfileResponse getProfile(String id) throws AppCheckedException;

    List<PostResponse> findByText(String text, String userId) throws AppCheckedException;

    PostResponse getPostById(String postId, String userId) throws AppCheckedException;

    List<PostStatisticsDTO> countStatisticsPostToday(java.time.LocalDateTime startDate, java.time.LocalDateTime endDate);

    List<PostStatisticsLongDateDTO> countStatisticsPostLongDay(java.time.LocalDateTime startDate, java.time.LocalDateTime endDate);

    List<PostResponse> getPostByFollowing(String userId);
}
