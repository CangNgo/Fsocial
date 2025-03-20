package com.fsocial.messageservice.repository.impl;

import com.fsocial.messageservice.Entity.Message;
import com.fsocial.messageservice.repository.MessageRepositoryCustom;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MessageRepositoryCustomImpl implements MessageRepositoryCustom {
    MongoTemplate mongoTemplate;

    @Override
    public void updateMessagesAsRead(String conversationId, String userId) {
        Query query = new Query(Criteria.where("conversationId").is(conversationId)
                .and("receiverId").is(userId) // Chỉ cập nhật tin nhắn được gửi đến receiverId
                .and("isRead").is(false)); // Chỉ cập nhật tin nhắn chưa đọc

        Update update = new Update().set("isRead", true);

        mongoTemplate.updateMulti(query, update, Message.class);
    }
}
