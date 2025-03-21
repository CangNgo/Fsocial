package com.fsocial.messageservice.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fsocial.messageservice.Entity.Message;
import com.fsocial.messageservice.dto.request.MessageRequest;
import com.fsocial.messageservice.dto.response.MessageResponse;
import com.fsocial.messageservice.mapper.MessageMapper;
import com.fsocial.messageservice.repository.MessageRepository;
import com.fsocial.messageservice.services.CacheService;
import com.fsocial.messageservice.services.ChatService;
import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.DelayQueue;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ChatServiceImpl implements ChatService {
    RedisTemplate<String, String> stringRedisTemplate;
    MessageRepository messageRepository;
    MessageMapper messageMapper;
    ObjectMapper objectMapper;
    DelayQueue<DelayedMessage> delayQueue = new DelayQueue<>();
    CacheService cacheService;

    String MESSAGE_QUEUE_KEY_PREFIX = "chat:messages:";

    @PostConstruct
    private void startFlushWorker() {
        Thread workerThread = new Thread(() -> {
            while (true) {
                try {
                    DelayedMessage delayedMessage = delayQueue.take();
                    flushMessagesToDB(delayedMessage.getConversationId());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        workerThread.setDaemon(true);
        workerThread.start();
    }

    @Override
    public MessageResponse cacheChatMessage(MessageRequest request) {
        // Kiểm tra sự tồn tại của receiverId và conversationId
        cacheService.validateUser(request.getReceiverId());
        cacheService.ensureConversationExists(request.getConversationId());

        String cacheKey = MESSAGE_QUEUE_KEY_PREFIX + request.getConversationId();
        stringRedisTemplate.opsForList().rightPush(cacheKey, convertToJson(request));
        long size = Optional.ofNullable(stringRedisTemplate.opsForList().size(cacheKey)).orElse(0L);
        int BATCH_SIZE = 5;
        if (size >= BATCH_SIZE) {
            flushMessagesToDB(request.getConversationId());
        } else {
            long FLUSH_DELAY = 3_000; // 3 giây
            delayQueue.offer(new DelayedMessage(request.getConversationId(), FLUSH_DELAY));
        }

        return MessageResponse.builder()
                .content(request.getContent())
                .conversationId(request.getConversationId())
                .receiverId(request.getReceiverId())
                .isRead(false)
                .createAt(request.getCreateAt())
                .build();
    }

    public void flushMessagesToDB(String conversationId) {
        String cacheKey = MESSAGE_QUEUE_KEY_PREFIX + conversationId;

        List<String> messageJsonList = stringRedisTemplate.opsForList().range(cacheKey, 0, -1);
        if (messageJsonList == null || messageJsonList.isEmpty()) return;

        List<Message> messages = messageJsonList.stream()
                .map(message -> messageMapper.toEntity(convertFromJson(message)))
                .toList();

        messageRepository.saveAll(messages);
        stringRedisTemplate.delete(cacheKey);
    }

    private MessageRequest convertFromJson(String json) {
        try {
            return objectMapper.readValue(json, MessageRequest.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Lỗi khi chuyển đổi JSON thành MessageRequest", e);
        }
    }

    private <T> String convertToJson(T object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Lỗi khi chuyển đổi thành JSON", e);
        }
    }
}