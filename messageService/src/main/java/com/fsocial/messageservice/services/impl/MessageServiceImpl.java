package com.fsocial.messageservice.services.impl;

import com.fsocial.messageservice.Entity.Message;
import com.fsocial.messageservice.dto.request.MessageRequest;
import com.fsocial.messageservice.dto.response.LastMessage;
import com.fsocial.messageservice.dto.response.MessageResponse;
import com.fsocial.messageservice.enums.ErrorCode;
import com.fsocial.messageservice.exception.AppException;
import com.fsocial.messageservice.mapper.MessageMapper;
import com.fsocial.messageservice.repository.MessageRepository;
import com.fsocial.messageservice.repository.ConversationRepository;
import com.fsocial.messageservice.repository.httpClient.AccountClient;
import com.fsocial.messageservice.services.MessageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class MessageServiceImpl implements MessageService {
    MessageRepository messageRepository;
    MessageMapper messageMapper;
    ConversationRepository conversationRepository;
    AccountClient accountClient;
    RedisTemplate<String, Boolean> redisTemplate;

    @Override
    @Transactional(readOnly = true)
    public List<MessageResponse> findChatMessagesBetweenUsers(String conversationId, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize); // Lấy 20 tin nhắn mỗi trang (mới nhất trước)
        Page<Message> chatPage = messageRepository.findByConversationIdOrderByCreateAtDesc(conversationId, pageable);

        return chatPage.stream()
                .map(messageMapper::toMessageResponse)
                .toList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MessageResponse saveChatMessage(MessageRequest request) {
        validateUser(request.getReceiverId());
        ensureConversationExists(request.getConversationId());

        Message message = messageMapper.toEntity(request);
        Message savedMessage = messageRepository.save(message);

        return messageMapper.toMessageResponse(savedMessage);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMessagesByConversationId(String conversationId) {
        messageRepository.deleteAllByConversationId(conversationId);
        log.warn("Đã xoá tin nhắn trong Conversation {}", conversationId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMessage(String messageId) {
        if (!messageRepository.existsById(messageId)) throw new AppException(ErrorCode.NOT_FOUND);
        messageRepository.deleteById(messageId);
        log.info("Đã xoá tin nhắn với id: {}", messageId);
    }

    @Transactional
    @Override
    public void markMessagesAsRead(String conversationId) {
        List<Message> unreadMessages = messageRepository.findByConversationIdAndIsReadFalse(conversationId);

        if (!unreadMessages.isEmpty()) {
            unreadMessages.forEach(message -> message.setRead(true));
            messageRepository.saveAll(unreadMessages);
            log.info("Đã đánh dấu tin nhắn đã đọc trong cuộc trò chuyện {}", conversationId);
        } else {
            log.info("Không có tin nhắn chưa đọc trong cuộc trò chuyện {}", conversationId);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, LastMessage> findLastMessagesForConversations(List<String> conversationIds) {
        return messageRepository.findTopByConversationIdsOrderByCreateAtDesc(conversationIds)
                .stream()
                .collect(Collectors.toMap(
                        Message::getConversationId,
                        message -> new LastMessage(message.getContent(), message.isRead(), message.getCreateAt()),
                        (existing, replacement) -> existing.getCreateAt().isAfter(replacement.getCreateAt()) ? existing : replacement
                ));
    }

    private void validateUser(String userId) {
        String cacheKey = "user:" + userId;

        if (Boolean.TRUE.equals(redisTemplate.opsForValue().get(cacheKey))) {
            Long ttl = redisTemplate.getExpire(cacheKey, TimeUnit.MINUTES);
            if (ttl == null || ttl < 3) redisTemplate.expire(cacheKey, 5, TimeUnit.MINUTES);
            return;
        }

        if (!accountClient.validUserId(userId)) {
            log.error("Xác thực thất bại: userId {}", userId);
            throw new AppException(ErrorCode.ACCOUNT_NOT_EXISTED);
        }

        redisTemplate.opsForValue().set(cacheKey, true, 5, TimeUnit.MINUTES);
    }


    private void ensureConversationExists(String conversationId) {
        String cacheKey = "conversation:" + conversationId;

        if (Boolean.TRUE.equals(redisTemplate.opsForValue().get(cacheKey))) {
            Long ttl = redisTemplate.getExpire(cacheKey, TimeUnit.MINUTES);
            if (ttl == null || ttl < 3) redisTemplate.expire(cacheKey, 5, TimeUnit.MINUTES);
            return;
        }

        if (!conversationRepository.existsById(conversationId))
            throw new AppException(ErrorCode.CONVERSATION_NOT_EXIST);

        redisTemplate.opsForValue().set(cacheKey, true, 5, TimeUnit.MINUTES);
    }
}
