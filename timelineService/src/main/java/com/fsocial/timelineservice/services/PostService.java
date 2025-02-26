package com.fsocial.timelineservice.services;

import com.fsocial.timelineservice.dto.post.PostResponse;
import com.fsocial.timelineservice.dto.profile.ProfileResponse;
import com.fsocial.timelineservice.exception.AppCheckedException;

import java.util.List;

public interface PostService {
    List<PostResponse> getPosts() throws AppCheckedException;
    ProfileResponse getProfile(String id) throws AppCheckedException;
    List<PostResponse> findByText (String text) throws AppCheckedException;

}
