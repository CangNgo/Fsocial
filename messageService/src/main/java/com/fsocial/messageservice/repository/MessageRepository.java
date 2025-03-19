package com.fsocial.messageservice.repository;

import com.fsocial.messageservice.Entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends MongoRepository<Message, String> {
    Page<Message> findByConversationIdOrderByCreateAtDesc(String conversationId, Pageable pageable);
    void deleteAllByConversationId(String conversationId);
    Optional<Message> findTopByConversationIdOrderByCreateAtDesc(String conversationId);
    List<Message> findByConversationIdAndIsReadFalse(String conversationId);

    @Query(value = "{ 'conversationId': { $in: ?0 }, 'isRead': false }", sort = "{ 'createAt' : -1 }")
    List<Message> findTopByConversationIdsAndUnreadOrderByCreateAtDesc(List<String> conversationIds);

    @Query(value = "{ 'conversationId': { $in: ?0 }}", sort = "{ 'createAt' : -1 }")
    List<Message> findTopByConversationIdsOrderByCreateAtDesc(List<String> conversationIds);

    // Đếm số lượng tin nhắn chưa đọc thay vì lấy toàn bộ danh sách
    @Query(value = "{ 'conversationId': ?0, 'isRead': false }", count = true)
    long countUnreadMessages(String conversationId);

}
