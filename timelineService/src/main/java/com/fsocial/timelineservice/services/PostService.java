package com.fsocial.timelineservice.services;

import com.fsocial.timelineservice.dto.ApiResponse;
import com.fsocial.timelineservice.dto.post.PostByUserIdResponse;
import com.fsocial.timelineservice.dto.post.PostDTO;
import com.fsocial.timelineservice.dto.post.PostResponse;
import com.fsocial.timelineservice.dto.profile.ProfileResponse;
import com.fsocial.timelineservice.dto.profile.ProfileServiceResponse;
import com.fsocial.timelineservice.entity.Post;
import com.fsocial.timelineservice.exception.AppCheckedException;

import java.util.List;

public interface PostService {
    List<PostResponse> getPosts() throws AppCheckedException;
    List<PostByUserIdResponse> getPostsByUserId(String userId) throws AppCheckedException;
    ProfileResponse getProfile(String id) throws AppCheckedException;
    List<PostResponse> findByText (String text) throws AppCheckedException;

}
