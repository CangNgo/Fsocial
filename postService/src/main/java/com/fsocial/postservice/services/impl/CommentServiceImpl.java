package com.fsocial.postservice.services.impl;

import com.fsocial.event.NotificationRequest;
import com.fsocial.postservice.entity.Post;
import com.fsocial.postservice.enums.MessageNotice;
import com.fsocial.postservice.exception.AppCheckedException;
import com.fsocial.postservice.exception.StatusCode;
import com.fsocial.postservice.repository.CommentRepository;
import com.fsocial.postservice.dto.comment.CommentDTORequest;
import com.fsocial.postservice.entity.Comment;
import com.fsocial.postservice.entity.Content;
import com.fsocial.postservice.repository.PostRepository;
import com.fsocial.postservice.repository.httpClient.Accountclient;
import com.fsocial.postservice.services.CommentService;
import com.fsocial.postservice.services.UploadMedia;
import com.fsocial.postservice.services.KafkaService;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CommentServiceImpl implements CommentService {
    CommentRepository commentRepository;

    UploadMedia uploadMedia;
    KafkaService kafkaService;
    PostRepository postRepository;
    MongoTemplate mongoTemplate;
    Accountclient accountclient;

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
        commentRequest.setCreatDatetime(LocalDateTime.now());
        Comment savedComment = commentRepository.save(commentRequest);

        // Send request to notification
        String ownerId = post.getUserId();
        String userId = request.getUserId();
        kafkaService.sendNotification(NotificationRequest.builder()
                        .ownerId(ownerId)
                        .receiverId(userId)
                        .topic("notice-comment")
                        .message(MessageNotice.NOTIFICATION_COMMENT.getMessage())
                        .postId(postId)
                        .commentId(savedComment.getId())
                .build());

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
        // Kiểm tra xem người dùng đã thích bình luận chưa
        boolean existed = commentRepository.existsByIdAndLikes(commentId, userId);

        // Nếu chưa thích, thêm like và gửi thông báo
        if (!existed) {
            this.addLikeComment(commentId, userId);

            // Lấy thông tin bài đăng (postId) từ bình luận
            Comment comment = commentRepository.findById(commentId)
                    .orElseThrow(() -> new AppCheckedException("Bình luận không tồn tại", StatusCode.COMMENT_NOT_FOUND));
            String postId = comment.getPostId();  // Lấy postId từ bình luận

            // Gửi thông báo qua Kafka, gửi thêm postId và commentId
//            kafkaService.sendNotification(postId, userId, MessageNotice.NOTIFICATION_LIKE_COMMENT, postId, commentId);

            return true;
        } else {
            // Nếu đã thích, gỡ like và gửi thông báo
            this.removeLikeComment(commentId, userId);

            // Lấy thông tin bài đăng (postId) từ bình luận
            Comment comment = commentRepository.findById(commentId)
                    .orElseThrow(() -> new AppCheckedException("Bình luận không tồn tại", StatusCode.COMMENT_NOT_FOUND));
            String postId = comment.getPostId();  // Lấy postId từ bình luận

            // Gửi thông báo qua Kafka, gửi thêm postId và commentId
//            kafkaService.sendNotification(postId, userId, MessageNotice.NOTIFICATION_LIKE_COMMENT, postId, commentId);
//
            return false;
        }
    }

    @Override
    public Integer countLike(String commentId, String userId) {
        return 0;
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
}
