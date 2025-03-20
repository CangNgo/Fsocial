package com.fsocial.timelineservice.services.impl;

import com.fsocial.timelineservice.dto.profile.ProfileResponse;
import com.fsocial.timelineservice.dto.replyComment.ReplyCommentResponse;
import com.fsocial.timelineservice.enums.StatusCode;
import com.fsocial.timelineservice.exception.AppCheckedException;
import com.fsocial.timelineservice.repository.ReplyCommentRepository;
import com.fsocial.timelineservice.repository.httpClient.ProfileClient;
import com.fsocial.timelineservice.services.ReplyCommentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReplyCommentImpl implements ReplyCommentService {

    ProfileClient profileClient;

    ReplyCommentRepository replyCommentRepository;
    @Override
    public List<ReplyCommentResponse> getReplyCommentsByCommentId(String commentId) {
        return replyCommentRepository.findReplyCommentsByCommentId(commentId).stream()
                .map(replyComment -> {
                    ProfileResponse profileResponse = null;
                    try {
                        profileResponse = getProfile(replyComment.getUserId());
                    } catch (AppCheckedException e) {
                        throw new RuntimeException(e);
                    }

                    return ReplyCommentResponse.builder()
                            .id(replyComment.getId())
                            .commentId(replyComment.getCommentId())
                            .content(replyComment.getContent())
                            .countLikes(getCountLikesComment(replyComment.getId()))
                            .firstName(profileResponse.getFirstName())
                            .lastName(profileResponse.getLastName())
                            .avatar(profileResponse.getAvatar())
                            .userId(replyComment.getUserId())
                            .created_datetime(replyComment.getCreated_datetime())
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

    public int getCountLikesComment(String replyCommentId) {
        return replyCommentRepository.countLike(replyCommentId);
    }
}
