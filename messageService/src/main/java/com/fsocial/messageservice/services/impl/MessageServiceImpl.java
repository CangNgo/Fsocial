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
    public List<MessageResponse> findChatMessagesBetweenUsers(String conversationId, int page) {
        Pageable pageable = PageRequest.of(page, 20); // Lấy 20 tin nhắn mỗi trang (mới nhất trước)

        Page<Message> chatPage = messageRepository.findByConversationIdOrderByCreateAtDesc(conversationId, pageable);

        return chatPage.getContent().stream()
                .map(mess -> {
                    MessageResponse response = messageMapper.toMessageResponse(mess);
                    response.setReceiverId(mess.getReceiver());
                    return response;
                })
                .toList();
    }

    @Override
    @Transactional(rollbackFor = AppException.class)
    public MessageResponse saveChatMessage(MessageRequest request) {
        validateUser(request.getReceiverId());
        checkExistConversation(request.getConversationId());

        Message message = messageMapper.toEntity(request);
        message.setReceiver(request.getReceiverId());

        MessageResponse response = messageMapper.toMessageResponse(messageRepository.save(message));
        response.setReceiverId(message.getReceiver());
        return response;
    }


    @Override
    public void deleteMessagesByConversationId(String conversationId) {
        messageRepository.deleteAllByConversationId(conversationId);
        log.warn("Đã xoá thành công tất cả các tin nhắn trong Conversation {}", conversationId);
    }

    @Override
    @Transactional
    public void deleteMessage(String messageId) {
        Message message = messageRepository.findById(messageId).orElseThrow(
                () -> new AppException(ErrorCode.NOT_FOUND)
        );

        if (message != null) {
            messageRepository.deleteById(messageId);
            log.info("Đã xoá tin nhắn với id: {}", messageId);
        }
    }

    @Override
    public void markMessagesAsRead(String conversationId) {
        // Tìm tất cả tin nhắn chưa đọc trong conversation
        List<Message> unreadMessages = messageRepository.findByConversationIdAndIsReadFalse(conversationId);

        if (unreadMessages.isEmpty()) {
            log.info("Không có tin nhắn chưa đọc trong cuộc trò chuyện {}", conversationId);
            return;
        }

        unreadMessages.forEach(message -> message.setRead(true));
        messageRepository.saveAll(unreadMessages);

        log.info("Đã đánh dấu {} tin nhắn đã đọc trong cuộc trò chuyện {}", unreadMessages.size(), conversationId);
    }

    @Override
    public Map<String, LastMessage> findLastMessagesForConversations(List<String> conversationIds) {
        return messageRepository.findTopByConversationIdsOrderByCreateAtDesc(conversationIds)
                .stream()
                .collect(Collectors.toMap(
                        Message::getConversationId, // Lấy conversationId làm key
                        message -> LastMessage.builder()
                                .content(message.getContent())
                                .createAt(message.getCreateAt())
                                .isRead(message.isRead())
                                .build(),
                        (existing, replacement) -> existing.getCreateAt().isAfter(replacement.getCreateAt()) ? existing : replacement
                ));
    }

    private void validateUser(String userId) {
        String userCacheKey = "user:" + userId;
        if (Boolean.TRUE.equals(redisTemplate.hasKey(userCacheKey))) {
            redisTemplate.expire(userCacheKey, 5, TimeUnit.MINUTES);
            return;
        }

        if (!accountClient.validUserId(userId)) {
            log.error("Xác thực người dùng không thành công cho userId: {}", userId);
            throw new AppException(ErrorCode.ACCOUNT_NOT_EXISTED);
        }

        redisTemplate.opsForValue().set(userCacheKey, true, 5, TimeUnit.MINUTES);
    }

    private void checkExistConversation(String conversationId) {
        String conversationKey = "conversation:" + conversationId;
        if (Boolean.FALSE.equals(redisTemplate.hasKey(conversationKey))) {
            if (!conversationRepository.existsById(conversationId))
                throw new AppException(ErrorCode.CONVERSATION_NOT_EXIST);

            redisTemplate.opsForValue().set(conversationKey, true, 5, TimeUnit.MINUTES);
        }

        redisTemplate.expire(conversationKey, 5, TimeUnit.MINUTES);
    }
}