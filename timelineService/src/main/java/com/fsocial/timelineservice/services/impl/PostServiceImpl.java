package com.fsocial.timelineservice.services.impl;

import com.fsocial.timelineservice.Repository.CommentRepository;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostServiceImpl implements PostService {

    PostRepository postRepository;

    ProfileClient profileClient;

    CommentRepository commentRepository;

    @Override
    public List<PostResponse> getPosts() throws AppCheckedException {
        return postRepository.findAll().stream()
                .map(post -> {
                    ProfileResponse profile = null;
                    Integer countComment = null;
                    try {
                        profile = getProfile(post.getUserId());
                        countComment = commentRepository.countCommentsByPostId(post.getId());
                    } catch (AppCheckedException e) {
                        throw new RuntimeException(e);
                    }
                    return PostResponse.builder()
                            .id(post.getId())
                            .content(post.getContent())
                            .countLikes(post.getCountLikes())
                            .countComments(countComment)
                            .userId(post.getUserId())
                            .userName(profile.getFirstName() + " " + profile.getLastName())
                            .avatar(profile.getAvatar())
                            .createdAt(post.getCreatedAt())
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Override
    public ProfileResponse getProfile(String userId) throws AppCheckedException {
        try {
            return profileClient.getProfile(userId);
        } catch (Exception e) {
            throw new AppCheckedException(e.getMessage(), StatusCode.USER_NOT_FOUND);
        }
    }


}
