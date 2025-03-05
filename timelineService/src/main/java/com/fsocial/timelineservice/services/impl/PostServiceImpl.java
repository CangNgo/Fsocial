package com.fsocial.timelineservice.services.impl;

import com.fsocial.timelineservice.dto.post.PostResponse;
import com.fsocial.timelineservice.dto.profile.ProfileResponse;
import com.fsocial.timelineservice.entity.Post;
import com.fsocial.timelineservice.enums.StatusCode;
import com.fsocial.timelineservice.exception.AppCheckedException;
import com.fsocial.timelineservice.exception.AppUnCheckedException;
import com.fsocial.timelineservice.repository.CommentRepository;
import com.fsocial.timelineservice.repository.PostRepository;
import com.fsocial.timelineservice.repository.httpClient.ProfileClient;
import com.fsocial.timelineservice.services.PostService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
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
    public List<PostResponse> getPostsByUserId(String userId) throws AppUnCheckedException {
        return postRepository.findAll().stream()
                .map(post -> {
                    try {
                        return this.mapToPostByUserIdResponse(post,userId);
                    } catch (AppCheckedException e) {
                        throw new RuntimeException(e.getMessage());
                    }
                })
                .collect(Collectors.toList());
    }

    private PostResponse mapToPostResponse(Post post) throws AppCheckedException {
        ProfileResponse profile = getProfile(post.getUserId());
        return PostResponse.builder()
                .id(post.getId())
                .content(post.getContent())
                .countLikes(getCountLikes(post.getId()))
                .countComments(getCountComment(post.getId()))
                .userId(post.getUserId())
                .displayName(profile.getFirstName() + " " + profile.getLastName())
                .avatar(profile.getAvatar())
                .createDatetime(post.getCreateDatetime())
                .isLike(false)
                .build();
    }

    private PostResponse mapToPostByUserIdResponse(Post post, String userId) throws AppCheckedException {
        ProfileResponse profile = getProfile(post.getUserId());
        boolean likePost = postRepository.existsByIdAndLikes(post.getId(),post.getUserId());
        return PostResponse.builder()
                .id(post.getId())
                .content(post.getContent())
                .countLikes(getCountLikes(post.getId()))
                .countComments(getCountComment(post.getId()))
                .userId(post.getUserId())
                .displayName(profile.getFirstName() + " " + profile.getLastName())
                .avatar(profile.getAvatar())
                .createDatetime(post.getCreateDatetime())
                .isLike(likePost)
                .build();
    }

    @Override
    public ProfileResponse getProfile(String userId) throws AppCheckedException {
        try {
            return profileClient.getProfileResponseByUserId(userId);
        } catch (Exception e) {
            throw new AppCheckedException("Không tìm thấy thông tin người dùng: " + userId, StatusCode.USER_NOT_FOUND);
        }
    }

    @Override
    public List<PostResponse> findByText(String text) {
        return postRepository.findByContentTextContainingIgnoreCase(text).stream()
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
                            .createDatetime(post.getCreateDatetime())
                            .build();
                })
                .collect(Collectors.toList());
    }

    private int getCountLikes (String postId){
        return postRepository.countLikeByPost(postId);
    }
    private int getCountComment (String postId){
        return commentRepository.countCommentsByPostId(postId);
    }

}
