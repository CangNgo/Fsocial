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

    @Query(value = "{ 'conversationId': { $in: ?0 } }", sort = "{ 'createAt' : -1 }")
    List<Message> findTopByConversationIdsOrderByCreateAtDesc(List<String> conversationIds);
}
