package com.fsocial.timelineservice.services.impl;

import com.fsocial.timelineservice.Repository.PostRepository;
import com.fsocial.timelineservice.Repository.httpClient.ProfileClient;
import com.fsocial.timelineservice.dto.ApiResponse;
import com.fsocial.timelineservice.dto.post.PostDTO;
import com.fsocial.timelineservice.dto.post.PostResponse;
import com.fsocial.timelineservice.dto.profile.ProfileResponse;
import com.fsocial.timelineservice.dto.profile.ProfileServiceResponse;
import com.fsocial.timelineservice.entity.Post;
import com.fsocial.timelineservice.exception.AppCheckedException;
import com.fsocial.timelineservice.exception.AppUnCheckedException;
import com.fsocial.timelineservice.exception.StatusCode;
import com.fsocial.timelineservice.mapper.PostMapper;
import com.fsocial.timelineservice.services.PostService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostServiceImpl implements PostService {

    PostRepository postRepository;

    PostMapper postMapper;

    ProfileClient profileClient;


    @Override
    public List<PostResponse> getPosts() throws AppCheckedException {

        List<PostResponse> result = new ArrayList<>();

        List<Post> posts = postRepository.findAll();

        for (Post post : posts) {

            ProfileResponse profileResponse = getProfile(post.getUserId());
            String userName = profileResponse.getFirstName()+ " " + profileResponse.getLastName();
            result.add(PostResponse.builder()
                            .id(post.getId())
                            .content(post.getContent())
                            .countLikes(post.getCountLikes())
                            .userId(post.getUserId())
                            .userName(userName)
                            .avatar(profileResponse.getAvatar())
                    .build());
        }

        return result;
    }

    @Override
    public ProfileResponse getProfile(String userId) throws AppCheckedException {
        try {
            ApiResponse<ProfileResponse> profileResponse = profileClient.getProfile(userId);
            return profileResponse.getData();
        } catch (Exception e) {
            throw new AppCheckedException(e.getMessage(), StatusCode.USER_NOT_FOUND);
        }
    }


}
