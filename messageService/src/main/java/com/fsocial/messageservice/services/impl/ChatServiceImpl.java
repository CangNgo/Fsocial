package com.fsocial.messageservice.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fsocial.messageservice.Entity.Message;
import com.fsocial.messageservice.dto.ApiResponse;
import com.fsocial.messageservice.dto.request.ActionsRequest;
import com.fsocial.messageservice.dto.request.MessageRequest;
import com.fsocial.messageservice.dto.response.ActionsResponse;
import com.fsocial.messageservice.dto.response.MessageResponse;
import com.fsocial.messageservice.enums.ErrorCode;
import com.fsocial.messageservice.enums.ResponseStatus;
import com.fsocial.messageservice.enums.TypesAction;
import com.fsocial.messageservice.exception.AppException;
import com.fsocial.messageservice.mapper.MessageMapper;
import com.fsocial.messageservice.repository.MessageRepository;
import com.fsocial.messageservice.services.CacheService;
import com.fsocial.messageservice.services.ChatService;
import com.fsocial.messageservice.services.MessageService;
import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.TimeUnit;

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
    MessageService messageService;

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
        String messageId = new ObjectId().toHexString();
        request.setMessageId(messageId);

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
                .messageId(messageId)
                .content(request.getContent())
                .conversationId(request.getConversationId())
                .receiverId(request.getReceiverId())
                .isRead(false)
                .createAt(request.getCreateAt())
                .build();
    }

    @Override
    public ApiResponse<?> handleChatActions(ActionsRequest request) {
        TypesAction typesAction = request.getType();
        return switch (typesAction) {
            case TYPING -> processTypingAction(request);
            case RECALL -> processRecallAction(request);
            case MARKREAD -> {
                messageService.markMessagesAsRead(request.getConversationId(), request.getSenderId());
                yield ApiResponse.buildApiResponse(null, ResponseStatus.SUCCESS);
            }
            default -> {
                log.error("Loại hành động không hợp lệ.");
                throw new AppException(ErrorCode.NOT_FOUND);
            }
        };
    }

    private ApiResponse<ActionsResponse> processTypingAction(ActionsRequest request) {
        String senderId = Optional.ofNullable(request.getSenderId())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_NULL));
        String conversationId = Optional.ofNullable(request.getConversationId())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_NULL));

        String TYPING_KEY_PREFIX = "typing:";
        long TYPING_TIMEOUT = 3; // 3 giây

        String typingKey = TYPING_KEY_PREFIX + conversationId + ":" + senderId;

        // Lưu trạng thái "typing" vào Redis với TTL = 3 giây
//        booleanRedisTemplate.opsForValue().set(typingKey, "true", TYPING_TIMEOUT, TimeUnit.SECONDS);

        ActionsResponse response = ActionsResponse.builder()
                .type(TypesAction.TYPING)
                .conversationId(conversationId)
                .senderId(senderId)
                .extraProperties(Map.of("typing", true))
                .build();

        return  ApiResponse.buildApiResponse(response, ResponseStatus.SUCCESS);
    }

    private ApiResponse<?> processRecallAction(ActionsRequest request) {
        String senderId = Optional.ofNullable(request.getSenderId())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_NULL));

        String messageId = Optional.ofNullable(request.getProperty("messageId"))
                .map(Object::toString)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_NULL));

        Message message = messageRepository.findById(messageId).orElseThrow(
                () -> {
                    log.error("Không tìm thấy Message với id={}", messageId);
                    return new AppException(ErrorCode.NOT_FOUND);
                }
        );

        validateMessageOwnership(message, senderId);

        String CONTENT_RECALL_MESSAGE = "Tin nhắn đã được thu hồi.";
        message.setContent(CONTENT_RECALL_MESSAGE);
        messageRepository.save(message);

        return ApiResponse.buildApiResponse(null, ResponseStatus.SUCCESS);
    }

    private void validateMessageOwnership(Message message, String senderId) {
        if (!Objects.equals(message.getReceiverId(), senderId)) {
            log.warn("Người gửi ({}) không có quyền thu hồi tin nhắn id={}.", senderId, message.getId());
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    private void flushMessagesToDB(String conversationId) {
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