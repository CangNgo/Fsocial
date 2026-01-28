package com.fsocial.postservice.services.impl;

import com.cloudinary.provisioning.Account;
import com.fsocial.postservice.dto.Response;
import com.fsocial.postservice.dto.profile.ProfileResponse;
import com.fsocial.postservice.dto.replyComment.LikeReplyCommentDTO;
import com.fsocial.postservice.dto.replyComment.ReplyCommentResponse;
import com.fsocial.postservice.dto.replyComment.ReplyCommentUpdateDTORequest;
import com.fsocial.postservice.entity.Comment;
import com.fsocial.postservice.entity.Post;
import com.fsocial.postservice.exception.AppCheckedException;
import com.fsocial.postservice.exception.StatusCode;
import com.fsocial.postservice.repository.CommentRepository;
import com.fsocial.postservice.repository.ReplyCommentRepository;
import com.fsocial.postservice.dto.replyComment.ReplyCommentRequest;
import com.fsocial.postservice.entity.Content;
import com.fsocial.postservice.entity.ReplyComment;
import com.fsocial.postservice.mapper.ReplyCommentMapper;
import com.fsocial.postservice.repository.httpClient.Accountclient;
import com.fsocial.postservice.repository.httpClient.ProfileClient;
import com.fsocial.postservice.services.ReplyCommentService;
import com.fsocial.postservice.services.UploadMedia;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor()
public class ReplyCommentServiceImpl implements ReplyCommentService {

    ReplyCommentRepository replyCommentRepository;

    ReplyCommentMapper replyCommentMapper;

    UploadMedia uploadMedia;

    Accountclient accountclient;

    MongoTemplate mongoTemplate;
    private final CommentRepository commentRepository;
    ProfileClient profileClient;

    @Override
    public ReplyComment addReplyComment(ReplyCommentRequest request) throws AppCheckedException {
        String[] uripostImage = new String[0];
        if (request.getMedia() != null && request.getMedia().length > 0) {
            MultipartFile[] validMedia = Arrays.stream(request.getMedia())
                    .filter(file -> file != null &&
                            !file.isEmpty() &&
                            file.getOriginalFilename() != null &&
                            !file.getOriginalFilename().isEmpty())
                    .toArray(MultipartFile[]::new);

            if (validMedia.length > 0) {
                uripostImage = uploadMedia.uploadMedia(validMedia);
            }
        }
        ;

        ReplyComment replyComment = replyCommentMapper.toEntity(request);
        replyComment.setContent(Content.builder()
                .media(uripostImage)
                .text(request.getText())
                .HTMLText(request.getHTMLText())
                .build());
        replyComment.setCreateDatetime(LocalDateTime.now());

        // cập nhật trạng thái thành true
        Comment comment = commentRepository.findById(request.getCommentId()).orElseThrow(
                () -> new AppCheckedException("Không tìm thấy comment ", StatusCode.COMMENT_NOT_FOUND));
        comment.setReply(true);
        commentRepository.save(comment);
        return replyCommentRepository.save(replyComment);
    }

    @Override
    public String deleteReplyComment(String idReplyComment) {
        replyCommentRepository.deleteById(idReplyComment);
        return "Xóa replycomment thành công";
    }

    @Override
    public ReplyComment updateReplyComment(ReplyCommentUpdateDTORequest upateReply) throws AppCheckedException {
        ReplyComment instance = replyCommentRepository.findById(upateReply.getReplyCommentId()).orElseThrow(
                () -> new AppCheckedException("Reply comment không tồn tại", StatusCode.REPLY_COMMENT_NOT_FOUND));
        if (userExists(upateReply.getUserId())) {
            throw new AppCheckedException("User không tồn tại", StatusCode.USER_NOT_FOUND);
        }
        instance.setContent(Content.builder()
                .text(upateReply.getText())
                .HTMLText(upateReply.getHTMLText())
                .build());
        return replyCommentRepository.save(instance);
    }

    @Override
    public boolean likeReplyComment(LikeReplyCommentDTO request) throws AppCheckedException {
        if (!replyCommentExists(request.getReplyCommentId())) {
            throw new AppCheckedException("Reply comment not found", StatusCode.REPLY_COMMENT_NOT_FOUND);
        }
        if (!userExists(request.getUserId())) {
            throw new AppCheckedException("User not found", StatusCode.USER_NOT_FOUND);
        }

        boolean exists = replyCommentRepository.existsByIdAndLikes(request.getReplyCommentId(), request.getUserId());

        try {
            if (!exists) {
                this.addLike(request.getReplyCommentId(), request.getUserId());
                // kafkaService.sendNotification(postId, userId,
                // MessageNotice.NOTIFICATION_LIKE);
                return true;
            } else {
                this.removeLike(request.getReplyCommentId(), request.getUserId());
                // kafkaService.sendNotification(postId, userId,
                // MessageNotice.NOTIFICATION_LIKE);
                return false;
            }
        } catch (RuntimeException e) {
            throw new RuntimeException("Lỗi khi like reply comment: " + e.getMessage());
        }

    }

    public void addLike(String postId, String userId) {
        Query query = new Query(Criteria.where("_id").is(postId));
        Update update = new Update().addToSet("likes", userId);
        mongoTemplate.updateFirst(query, update, ReplyComment.class);
    }

    public void removeLike(String postId, String userId) {
        Query query = new Query(Criteria.where("_id").is(postId));
        Update update = new Update().pull("likes", userId);
        mongoTemplate.updateFirst(query, update, ReplyComment.class);
    }

    private boolean userExists(String userId) {
        return accountclient.existsAccountByUserId(userId).getData().containsKey("exists");
    }

    private boolean replyCommentExists(String replyComment) {
        return replyCommentRepository.existsById(replyComment);
    }

    // Methods from timelineService
    @Override
    public List<com.fsocial.postservice.dto.replyComment.ReplyCommentResponse> getReplyCommentsByCommentId(
            String commentId) {
        return replyCommentRepository.findReplyCommentsByCommentId(commentId).stream()
                .map(replyComment -> {
                    com.fsocial.postservice.dto.profile.ProfileResponse profileResponse = null;
                    try {
                        profileResponse = profileClient.getProfileResponseByUserId(replyComment.getUserId());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                    return com.fsocial.postservice.dto.replyComment.ReplyCommentResponse.builder()
                            .id(replyComment.getId())
                            .commentId(replyComment.getCommentId())
                            .content(replyComment.getContent())
                            .countLikes(replyCommentRepository.countLike(replyComment.getId()) != null
                                    ? replyCommentRepository.countLike(replyComment.getId())
                                    : 0)
                            .firstName(profileResponse.getFirstName())
                            .lastName(profileResponse.getLastName())
                            .avatar(profileResponse.getAvatar())
                            .userId(replyComment.getUserId())
                            .createDatetime(replyComment.getCreateDatetime())
                            .build();
                })
                .collect(java.util.stream.Collectors.toList());
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
