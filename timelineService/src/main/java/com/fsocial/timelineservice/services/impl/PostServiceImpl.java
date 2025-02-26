package com.fsocial.timelineservice.services.impl;

import com.fsocial.timelineservice.dto.post.PostByUserIdResponse;
import com.fsocial.timelineservice.dto.post.PostResponse;
import com.fsocial.timelineservice.dto.profile.ProfileResponse;
import com.fsocial.timelineservice.entity.Post;
import com.fsocial.timelineservice.exception.AppCheckedException;
import com.fsocial.timelineservice.exception.AppUnCheckedException;
import com.fsocial.timelineservice.repository.CommentRepository;
import com.fsocial.timelineservice.repository.LikeRepository;
import com.fsocial.timelineservice.repository.PostRepository;
import com.fsocial.timelineservice.repository.httpClient.ProfileClient;
import com.fsocial.timelineservice.services.PostService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostServiceImpl implements PostService {

    PostRepository postRepository;

    ProfileClient profileClient;

    CommentRepository commentRepository;

    LikeRepository likeRepository;

    @Override
    public List<PostResponse> getPosts() throws AppUnCheckedException {
        return postRepository.findAll().stream()
                .map(post -> {
                    try {
                        return this.mapToPostResponse(post);
                    } catch (AppCheckedException e) {
                        throw new RuntimeException(e.getMessage());
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<PostByUserIdResponse> getPostsByUserId(String userId) throws AppUnCheckedException {
        return postRepository.findAll().stream()
                .map(post -> {
                    try {
                        return this.mapToPostResponse(post, userId);
                    } catch (AppCheckedException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }

    private PostResponse mapToPostResponse(Post post) throws AppCheckedException {
        ProfileResponse profile = getProfile(post.getUserId());
        Integer countComment = commentRepository.countCommentsByPostId(post.getId());
        return PostResponse.builder()
                .id(post.getId())
                .content(post.getContent())
                .countLikes(post.getCountLikes())
                .countComments(countComment)
                .userId(post.getUserId())
                .displayName(profile.getFirstName() + " " + profile.getLastName())
                .avatar(profile.getAvatar())
                .createdAt(post.getCreatedAt())
                .build();
    }

    @Override
    public ProfileResponse getProfile(String userId) throws AppCheckedException {
        try {
            return profileClient.getProfile(userId);
        } catch (Exception e) {
            throw new AppCheckedException("Không tìm thấy thông tin người dùng: " + userId, STATU.USER_NOT_FOUND);
        }
    }

    @Override
    public List<PostResponse> findByText(String text) {
        return postRepository.findByContentTextContaining(text).stream()
                .map(post -> {
                    ProfileResponse profile;
                    try {
                        profile = getProfile(post.getUserId());
                    } catch (AppCheckedException e) {
                        throw new RuntimeException(e);
                    }
                    Integer countComment = commentRepository.countCommentsByPostId(post.getId());

                    return PostResponse.builder()
                            .id(post.getId())
                            .content(post.getContent())
                            .countLikes(post.getCountLikes())
                            .countComments(countComment)
                            .userId(post.getUserId())
                            .displayName(profile.getFirstName() + " " + profile.getLastName())
                            .avatar(profile.getAvatar())
                            .createdAt(post.getCreatedAt())
                            .build();
                })
                .collect(Collectors.toList());
    }


}
