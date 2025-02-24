package com.fsocial.timelineservice.services.impl;

import com.fsocial.timelineservice.Repository.CommentRepository;
import com.fsocial.timelineservice.Repository.httpClient.ProfileClient;
import com.fsocial.timelineservice.dto.comment.CommentResponse;
import com.fsocial.timelineservice.dto.profile.ProfileResponse;
import com.fsocial.timelineservice.exception.AppCheckedException;
import com.fsocial.timelineservice.enums.ErrorCode;
import com.fsocial.timelineservice.services.CommentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    ProfileClient profileClient;

    CommentRepository commentRepository;

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

                    return CommentResponse.builder()
                            .id(comment.getId())
                            .content(comment.getContent())
                            .countReplyComments(comment.getCountReplyComment())
                            .countLikes(comment.getCountLikes())
                            .displayName(profileResponse.getFirstName() + " " + profileResponse.getLastName())
                            .avatar(profileResponse.getAvatar())
                            .userId(comment.getUserId())
                            .reply(comment.isReply())
                            .createdAt(comment.getCreatedAt())
                            .build();
                })
                .collect(Collectors.toList());
    }

    public ProfileResponse getProfile(String userId) throws AppCheckedException {

        try {
            return profileClient.getProfile(userId);
        } catch (Exception e) {
            throw new AppCheckedException("Không tìm thấy thông tin người dùng", ErrorCode.PROFILE_NOT_FOUND);
        }
    }
}
