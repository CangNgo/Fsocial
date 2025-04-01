package com.fsocial.notificationService.repository.impl;

import com.fsocial.notificationService.entity.Notification;
import com.fsocial.notificationService.repository.CustomNotificationRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class CustomNotificationRepositoryImpl implements CustomNotificationRepository {
    final MongoTemplate mongoTemplate;
    @Override
    public void markAllAsReadByUserId(String userId) {
        Query query = new Query(Criteria.where("ownerId").is(userId).and("read").is(false));
        Update update = new Update().set("read", true);
        mongoTemplate.updateMulti(query, update, Notification.class);
    }
}
