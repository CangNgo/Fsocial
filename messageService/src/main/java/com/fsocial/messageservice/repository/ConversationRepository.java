package com.fsocial.messageservice.repository;

import com.fsocial.messageservice.Entity.Conversation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationRepository extends MongoRepository<Conversation, String> {
    Optional<Conversation> findByParticipantsContaining(String userId);
    List<Conversation> findAllByParticipantsContaining(String userId);
    boolean existsByParticipants(List<String> participants);
}
