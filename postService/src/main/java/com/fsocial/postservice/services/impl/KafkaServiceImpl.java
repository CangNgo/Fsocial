package com.fsocial.postservice.services.impl;

import com.fsocial.event.NotificationRequest;
import com.fsocial.postservice.enums.MessageNotice;
import com.fsocial.postservice.repository.CommentRepository;
import com.fsocial.postservice.services.KafkaService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class KafkaServiceImpl implements KafkaService {
    KafkaTemplate<String, Object> kafkaTemplate;
    CommentRepository commentRepository;

    @Override
    public void sendNotification(String ownerId, String userId, MessageNotice messageNotice, String entityId, String commentId) {
        // Đặt postId mặc định là entityId
        String postId = entityId;

        // Nếu thông báo là về comment, cần lấy postId từ commentRepository
        if (messageNotice == MessageNotice.NOTIFICATION_LIKE_COMMENT) {
            postId = commentRepository.findPostIdByCommentId(entityId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy bài đăng cho comment"));
        }

        // Xây dựng NotificationRequest để gửi tới Kafka
        NotificationRequest noticeRequest = NotificationRequest.builder()
                .postId(postId)                // ID bài viết
                .commentId(commentId)          // ID bình luận (nếu có)
                .senderId(ownerId)             // ID người gửi thông báo
                .receiverId(userId)            // ID người nhận thông báo
                .message(messageNotice.getMessage())  // Nội dung thông báo
                .type(messageNotice.getTopic())      // Loại thông báo ("LIKE" hoặc "COMMENT")
                .build();

        // Gửi thông báo tới Kafka
        kafkaTemplate.send(messageNotice.getTopic(), noticeRequest);

        // Log thông báo đã được gửi
        log.info("Notification sent: OwnerId={}, ReceiverId={}, Topic={}, PostId={}, CommentId={}",
                ownerId, userId, messageNotice.getTopic(), postId, commentId);
    }
}
