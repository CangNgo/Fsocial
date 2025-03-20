package com.fsocial.messageservice.services.impl;

import com.fsocial.messageservice.Entity.Message;
import com.fsocial.messageservice.dto.request.MessageRequest;
import com.fsocial.messageservice.dto.response.LastMessage;
import com.fsocial.messageservice.dto.response.MessageListResponse;
import com.fsocial.messageservice.dto.response.MessageResponse;
import com.fsocial.messageservice.enums.ErrorCode;
import com.fsocial.messageservice.exception.AppException;
import com.fsocial.messageservice.mapper.MessageMapper;
import com.fsocial.messageservice.repository.MessageRepository;
import com.fsocial.messageservice.services.CacheService;
import com.fsocial.messageservice.services.MessageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class MessageServiceImpl implements MessageService {
    MessageRepository messageRepository;
    MessageMapper messageMapper;
    CacheService cacheService;

    @Override
    @Transactional(readOnly = true)
    public MessageListResponse findChatMessagesBetweenUsers(String conversationId, int page, String userId) {
        long unreadCount = messageRepository.countUnreadMessages(conversationId, userId);
        int pageSize = 30;

        if (unreadCount <= pageSize)
            return MessageListResponse.builder()
                    .listMessages(getListMessages(conversationId, page, pageSize))
                    .build();

        int totalPages = (int) Math.ceil((double) unreadCount / pageSize);

        List<MessageResponse> unreadMessages = new ArrayList<>();
        for (int i = 0; i < totalPages - 1; i++) {
            List<MessageResponse> result = getListMessages(conversationId, i, pageSize);
            unreadMessages.addAll(result);
        }

        return MessageListResponse.builder()
                .listMessages(unreadMessages)
                .page(totalPages - 1)
                .build();
    }

    private List<MessageResponse> getListMessages(String conversationId, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Message> chatPage = messageRepository.findByConversationIdOrderByCreateAtDesc(conversationId, pageable);

        return chatPage.stream()
                .map(messageMapper::toMessageResponse)
                .toList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MessageResponse saveChatMessage(MessageRequest request) {
        cacheService.validateUser(request.getReceiverId());
        cacheService.ensureConversationExists(request.getConversationId());

        Message message = messageMapper.toEntity(request);
        return messageMapper.toMessageResponse(messageRepository.save(message));
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void markMessagesAsRead(String conversationId, String userId) {
        messageRepository.updateMessagesAsRead(conversationId, userId);
        log.info("Đã đánh dấu tin nhắn là đã đọc trong cuộc trò chuyện {}", conversationId);
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
}