package com.fsocial.postservice.services.impl;

import com.fsocial.postservice.dto.comment.CommentDTO;
import com.fsocial.postservice.dto.comment.CommentDTORequest;
import com.fsocial.postservice.dto.comment.CommentResponse;
import com.fsocial.postservice.dto.comment.CommentUpdateDTORequest;
import com.fsocial.postservice.dto.post.PostDTO;
import com.fsocial.postservice.dto.profile.ProfileResponse;
import com.fsocial.postservice.entity.Comment;
import com.fsocial.postservice.entity.Content;
import com.fsocial.postservice.entity.Post;
import com.fsocial.postservice.exception.AppCheckedException;
import com.fsocial.postservice.exception.StatusCode;
import com.fsocial.postservice.repository.CommentRepository;
import com.fsocial.postservice.repository.PostRepository;
import com.fsocial.postservice.repository.httpClient.Accountclient;
import com.fsocial.postservice.repository.httpClient.ProfileClient;
import com.fsocial.postservice.services.CommentService;
import com.fsocial.postservice.services.RedisService;
import com.fsocial.postservice.services.UploadMedia;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CommentServiceImpl implements CommentService {
    CommentRepository commentRepository;
    UploadMedia uploadMedia;
    //    KafkaService kafkaService;
    PostRepository postRepository;
    MongoTemplate mongoTemplate;
    Accountclient accountclient;
    RedisService redisService;
    ProfileClient profileClient;

    @Override
    @Transactional
    public Comment addComment(CommentDTORequest request) throws AppCheckedException {
        String[] mediaUrls = extractValidMedia(request.getMedia());

        String postId = request.getPostId();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppCheckedException("Không tìm thấy bài đăng", StatusCode.POST_NOT_FOUND));

        Comment commentRequest = buildComment(request, mediaUrls);
        commentRequest.setCreatedAt(LocalDateTime.now());
        commentRequest.setLikes(new ArrayList<>());
        commentRequest.setCreateDatetime(LocalDateTime.now());
        Comment savedComment = commentRepository.save(commentRequest);

        // Send request to notification
        String ownerId = post.getOwner().getUserId();
        String userId = request.getUserId();

//        if (!Objects.equals(ownerId, userId)) {
//            kafkaService.sendNotification(NotificationRequest.builder()
//                    .ownerId(ownerId)
//                    .receiverId(userId)
//                    .topic(TopicKafka.TOPIC_COMMENT.getTopic())
//                    .postId(postId)
//                    .commentId(savedComment.getId())
//                    .build());
//        }

        //thêm vào personalization
        redisService.personalization(savedComment.getUserId(), post.getOwner().getUserId());

        return savedComment;
    }

    private String[] extractValidMedia(MultipartFile[] media) throws AppCheckedException {
        if (media == null || media.length == 0) return new String[0];

        try {
            MultipartFile[] validMedia = Arrays.stream(media)
                    .filter(file -> file != null && !file.isEmpty()
                            && file.getOriginalFilename() != null
                            && !file.getOriginalFilename().isEmpty())
                    .toArray(MultipartFile[]::new);

            return validMedia.length > 0 ? uploadMedia.uploadMedia(validMedia) : new String[0];
        } catch (Exception e) {
            log.error("Lỗi khi tải lên tệp: {}", e.getMessage(), e);
            throw new AppCheckedException("Upload hình ảnh thất bại", StatusCode.UPLOAD_MEDIA_FAILED);
        }
    }

    private Comment buildComment(CommentDTORequest request, String[] mediaUrls) {
        return Comment.builder()
                .likes(new ArrayList<>())
                .reply(false)
                .postId(request.getPostId())
                .userId(request.getUserId())
                .content(Content.builder()
                        .text(request.getText())
                        .media(mediaUrls)
                        .HTMLText(request.getHTMLText())
                        .build())
                .build();
    }

    @Override
    public boolean toggleLikeComment(String commentId, String userId) throws AppCheckedException {
        boolean existed = commentRepository.existsByIdAndLikes(commentId, userId);
        if (!existed) {
            this.addLikeComment(commentId, userId);
            return true;
        } else {
            this.removeLikeComment(commentId, userId);
            return false;
        }
    }

    @Override
    public Integer countLike(String commentId, String userId) {
        return 0;
    }

    @Override
    public Comment updateComment(CommentUpdateDTORequest comment) throws AppCheckedException {
        if (userExists(comment.getUserId()))
            throw new AppCheckedException("User không tồn tại", StatusCode.USER_NOT_FOUND);

        Comment update = commentRepository.findById(comment.getCommentId()).orElseThrow(() -> new AppCheckedException("Không tìm thấy comment", StatusCode.COMMENT_NOT_FOUND));
        //cập nhật text
        update.setContent(Content.builder()
                .HTMLText(comment.getHTMLText())
                .text(comment.getText())
                .build());
        return commentRepository.save(update);
    }

    @Override
    public String deleteComment(String commentID) {
        commentRepository.deleteById(commentID);
        return "Xóa comment thành công";
    }

    public void addLikeComment(String commentId, String userId) throws AppCheckedException {
        boolean check = this.userExists(userId);
        if (!this.commentExist(commentId))
            throw new AppCheckedException("Bình luân không tồn tại", StatusCode.COMMENT_NOT_FOUND);
        if (!this.userExists(userId))
            throw new AppCheckedException("Tài khoản người dùng không tồn tại", StatusCode.USER_NOT_FOUND);

        Query query = new Query(Criteria.where("_id").is(commentId));
        Update update = new Update().addToSet("likes", userId);
        mongoTemplate.updateFirst(query, update, Comment.class);

    }

    public void removeLikeComment(String commentId, String userId) throws AppCheckedException {
        if (!this.commentExist(commentId))
            throw new AppCheckedException("Bình luân không tồn tại", StatusCode.COMMENT_NOT_FOUND);
        if (!this.userExists(userId))
            throw new AppCheckedException("Tài khoản người dùng không tồn tại", StatusCode.USER_NOT_FOUND);

        Query query = new Query(Criteria.where("_id").is(commentId));
        Update update = new Update().pull("likes", userId);
        mongoTemplate.updateFirst(query, update, Comment.class);
    }

    public boolean userExists(String userId) {
        return accountclient.existsAccountByUserId(userId).getData().containsKey("exists");
    }

    public boolean commentExist(String commentId) {
        return commentRepository.existsById(commentId);
    }

    private CommentResponse convertToCommentResponse(Comment comment, com.fsocial.postservice.dto.profile.ProfileResponse profileResponse, String currentUserId) {
        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .countLikes(commentRepository.countLike(comment.getId()) != null ? commentRepository.countLike(comment.getId()) : 0)
                .firstName(profileResponse != null ? profileResponse.getFirstName() : "")
                .lastName(profileResponse != null ? profileResponse.getLastName() : "")
                .avatar(profileResponse != null ? profileResponse.getAvatar() : "")
                .userId(comment.getUserId())
                .reply(comment.getReply())
                .like(comment.getLikes() != null && comment.getLikes().contains(currentUserId))
                .createDatetime(comment.getCreateDatetime())
                .build();
    }

    public CommentResponse convertToCommentResponse(Comment comment) {
        com.fsocial.postservice.dto.profile.ProfileResponse profileResponse = null;
        try {
            profileResponse = profileClient.getProfileResponseByUserId(comment.getUserId());
        } catch (Exception e) {
            log.error("Error getting profile for user {}", comment.getUserId(), e);
        }
        String currentUserId = null;
        try {
            currentUserId = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName();
        } catch (Exception e) {
            log.debug("No authentication context available");
        }
        return convertToCommentResponse(comment, profileResponse, currentUserId);
    }

    @Override
    public List<CommentDTO> deleteCommentByPostId(String postId) throws AppCheckedException {

        return commentRepository.deleteByPostId(postId);
    }

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
                            .reply(comment.getReply())
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
