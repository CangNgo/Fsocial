package com.fsocial.timelineservice.services.impl;

import com.fsocial.timelineservice.Repository.CommentRepository;
import com.fsocial.timelineservice.Repository.httpClient.ProfileClient;
import com.fsocial.timelineservice.dto.comment.CommentResponse;
import com.fsocial.timelineservice.dto.profile.ProfileResponse;
import com.fsocial.timelineservice.exception.AppCheckedException;
import com.fsocial.timelineservice.exception.StatusCode;
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
        return commentRepository.findAll().stream()
                .map(comment -> {
                    ProfileResponse profileResponse = null;
                    try {
                        profileResponse = getProfile(comment.getUserId());
                    } catch (RuntimeException e) {
                        throw new RuntimeException(e);
                    }
                    return CommentResponse.builder()
                            .id(comment.getId())
                            .content(comment.getContent())
                            .countReplyComments(comment.getCountReplyComment())
                            .countLikes(comment.getCountLikes())
                            .userName(profileResponse.getFirstName() + " " + profileResponse.getLastName())
                            .avatar(profileResponse.getAvatar())
                            .userId(comment.getUserId())
                            .reply(comment.isReply())
                            .build();
                })
                .collect(Collectors.toList());
    }

    public ProfileResponse getProfile(String userId) {
        return profileClient.getProfile(userId);
    }
}
