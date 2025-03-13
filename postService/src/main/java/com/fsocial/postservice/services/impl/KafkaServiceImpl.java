package com.fsocial.postservice.services.impl;

import com.fsocial.event.NotificationRequest;
import com.fsocial.postservice.enums.MessageNotice;
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

    @Override
    public void sendNotification(String ownerId, String userId, MessageNotice messageNotice, String postId, String commentId) {
        NotificationRequest noticeRequest = NotificationRequest.builder()
                .ownerId(ownerId)
                .receiverId(userId)
                .message(messageNotice.getMessage())
                .topic(messageNotice.getTopic())
                .postId(postId)  // Gửi thêm postId
                .commentId(commentId)  // Gửi thêm commentId
                .build();

        kafkaTemplate.send(messageNotice.getTopic(), noticeRequest);
        log.info("Notification sent: OwnerId={}, ReceiverId={}, Topic={}, PostId={}, CommentId={}",
                ownerId, userId, messageNotice.getTopic(), postId, commentId);
    }

}
