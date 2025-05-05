package com.fsocial.timelineservice.services;

import com.fsocial.timelineservice.dto.post.PostResponse;
import com.fsocial.timelineservice.dto.post.PostStatisticsDTO;
import com.fsocial.timelineservice.dto.post.PostStatisticsLongDateDTO;
import com.fsocial.timelineservice.dto.profile.ProfileResponse;
import com.fsocial.timelineservice.exception.AppCheckedException;

import java.time.LocalDateTime;
import java.util.List;

public interface PostService {
    //    List<PostResponse> getPosts() throws AppCheckedException;
    List<PostResponse> getPostsByUserId(String userId) throws AppCheckedException;

    ProfileResponse getProfile(String id) throws AppCheckedException;

    List<PostResponse> findByText(String text, String userId) throws AppCheckedException;

    PostResponse getPostById(String postId, String userId) throws AppCheckedException;

    List<PostStatisticsDTO> countStatisticsPostToday(LocalDateTime startDate, LocalDateTime endDate);

    List<PostStatisticsLongDateDTO> countStatisticsPostLongDay(LocalDateTime startDate, LocalDateTime endDate);

    List<PostResponse> getPostByFollowing(String userId);
}
