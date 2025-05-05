package com.fsocial.timelineservice.services.impl;

import com.fsocial.timelineservice.enums.StatusCode;
import com.fsocial.timelineservice.repository.CommentRepository;
import com.fsocial.timelineservice.repository.httpClient.ProfileClient;
import com.fsocial.timelineservice.dto.comment.CommentResponse;
import com.fsocial.timelineservice.dto.profile.ProfileResponse;
import com.fsocial.timelineservice.exception.AppCheckedException;
import com.fsocial.timelineservice.services.CommentService;
import com.fsocial.timelineservice.services.RedisService;
import jakarta.servlet.ServletContext;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    ProfileClient profileClient;

    CommentRepository commentRepository;

    RedisService redisService;
    ServletContext servletContext;

    @Override
    public List<CommentResponse> getComments(String postId) {
        return commentRepository.findCommentsByPostId(postId).stream()
                .map(comment -> {
                    ProfileResponse profileResponse = null;
                    try {
                        profileResponse = getProfile(comment.getUserId());
                    } catch (AppCheckedException e) {
                        throw new RuntimeException(e);
                    }
                    String userId = SecurityContextHolder.getContext().getAuthentication().getName();
                    System.out.println("userId: " + userId);
                    return CommentResponse.builder()
                            .id(comment.getId())
                            .content(comment.getContent())
                            .countLikes(getCountLikesComment(comment.getId()))
                            .firstName(profileResponse.getFirstName())
                            .lastName(profileResponse.getLastName())
                            .avatar(profileResponse.getAvatar())
                            .userId(comment.getUserId())
                            .reply(comment.isReply())
                            .like(comment.getLikes().contains(userId))
                            .createDatetime(comment.getCreateDatetime())
                            .build();
                })
                .collect(Collectors.toList());
    }

    public ProfileResponse getProfile(String userId) throws AppCheckedException {

        try {
            return profileClient.getProfileResponseByUserId(userId);
        } catch (Exception e) {
            throw new AppCheckedException("Không tìm thấy thông tin người dùng", StatusCode.PROFILE_NOT_FOUND);
        }
    }

    private int getCountLikesComment(String commentId) {
        Integer count = commentRepository.countLike(commentId);
        return count != null ? count : 0;
    }

    private int getCountComments(String postId) {
        return commentRepository.countCommentsByPostId(postId);
    }
}
