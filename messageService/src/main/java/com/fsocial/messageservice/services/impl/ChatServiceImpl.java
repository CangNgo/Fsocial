package com.fsocial.messageservice.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fsocial.messageservice.Entity.Message;
import com.fsocial.messageservice.dto.ApiResponse;
import com.fsocial.messageservice.dto.request.ActionsRequest;
import com.fsocial.messageservice.dto.request.MessageRequest;
import com.fsocial.messageservice.dto.response.ActionsResponse;
import com.fsocial.messageservice.dto.response.MessageRecallResponse;
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
import com.fsocial.messageservice.util.RedissonLock;
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
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    RedissonLock redissonLock;

    static final String MESSAGE_QUEUE_KEY_PREFIX = "chat:messages:";
    static final int BATCH_SIZE = 5; // limited 5 message in Redis
    static final long FLUSH_DELAY = 3000L; // 3 seconds

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

        MessageResponse response = messageMapper.toMessageResponse(request);
        response.setImages(createImageMap(request.getImages()));
        response.setRead(false);
        response.setMessageId(new ObjectId().toHexString());

        // Set Object vào Redis
        String cacheKey = MESSAGE_QUEUE_KEY_PREFIX + request.getConversationId();
        stringRedisTemplate.opsForList().rightPush(cacheKey, convertObjectToJson(response));

        // Kiểm tra số lượng Message được ở Redis
        Long size = stringRedisTemplate.opsForList().size(cacheKey);
        if (size != null && size >= BATCH_SIZE) {
            flushMessagesToDB(request.getConversationId());
        } else {
            delayQueue.offer(new DelayedMessage(request.getConversationId(), FLUSH_DELAY));
        }

        return response;
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
            case REACTION -> processReactionMessage(request);
            default -> {
                log.error("Loại hành động không hợp lệ.");
                throw new AppException(ErrorCode.NOT_FOUND);
            }
        };
    }

    private Map<String, String> createImageMap(List<String> images) {
        if (images == null || images.isEmpty()) return Collections.emptyMap();

        return IntStream.range(0, images.size())
                .boxed()
                .collect(Collectors.toMap(i -> "image:" + (i + 1),
                        images::get, (a, b) -> b,
                        LinkedHashMap::new));
    }

    private ApiResponse<ActionsResponse> processReactionMessage(ActionsRequest request) {
        Message message = validActionForMessage(request);
        String newReaction = getRequiredProperty(request.getAllProperties(), "reaction");

        updateReactionForMessage(message, newReaction);

        ActionsResponse response = ActionsResponse.builder()
                .type(TypesAction.REACTION)
                .senderId(request.getSenderId())
                .conversationId(request.getConversationId())
                .build();
        response.setExtraProperties(Map.of(
                "messageId", getRequiredProperty(request.getAllProperties(), "messageId"),
                "reaction", message.getReaction()
        ));

        return ApiResponse.buildApiResponse(response, ResponseStatus.SUCCESS);
    }

    private String getRequiredProperty(Map<String, Object> properties, String key) {
        return Optional.ofNullable(properties.get(key))
                .map(Object::toString)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_NULL));
    }

    private Message validActionForMessage(ActionsRequest request) {
        Optional.ofNullable(request.getSenderId())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_NULL));

        String messageId = getRequiredProperty(request.getAllProperties(), "messageId");

        return findMessageWithMessageIdFormUser(messageId);
    }

    private void updateReactionForMessage(Message message, String newReaction) {
        if (Objects.equals(newReaction, message.getReaction())) {
            message.setReaction(null); // Unlike nếu reaction giống nhau
        } else {
            message.setReaction(newReaction); // Cập nhật nếu reaction khác nhau
        }
        messageRepository.save(message);
        log.info("Cập nhật reaction thành công: {}", newReaction);
    }

    private ApiResponse<ActionsResponse> processTypingAction(ActionsRequest request) {
        String senderId = Optional.ofNullable(request.getSenderId())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_NULL));
        String conversationId = Optional.ofNullable(request.getConversationId())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_NULL));

        ActionsResponse response = ActionsResponse.builder()
                .type(TypesAction.TYPING)
                .conversationId(conversationId)
                .senderId(senderId)
                .build();
        response.addProperty("typing", true);

        return  ApiResponse.buildApiResponse(response, ResponseStatus.SUCCESS);
    }

    private ApiResponse<MessageRecallResponse> processRecallAction(ActionsRequest request) {
        Message message = validActionForMessage(request);
        validateMessageOwnership(message.getReceiverId(), request.getSenderId());
        message.setContent(null);
        Message messageRecall = messageRepository.save(message);

        return ApiResponse.buildApiResponse(MessageRecallResponse.builder()
                        .messageId(messageRecall.getId())
                        .senderId(request.getSenderId())
                .build(),
                ResponseStatus.SUCCESS);
    }

    private Message findMessageWithMessageIdFormUser(String messageId) {
        return messageRepository.findById(messageId)
               .orElseThrow(() -> {
                    log.error("Không tìm thấy tin nhắn với id={}", messageId);
                    return new AppException(ErrorCode.NOT_FOUND);
                });
    }

    private void validateMessageOwnership(String ownerId, String senderId) {
        if (!Objects.equals(ownerId, senderId)) {
            log.warn("Người gửi ({}) không có quyền thu hồi tin nhắn.", senderId);
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    private void flushMessagesToDB(String conversationId) {
        String cacheKey = MESSAGE_QUEUE_KEY_PREFIX + conversationId;
        String lockKey = "lock:flushMessages:" + conversationId;
        if (!redissonLock.acquireLock(lockKey)) {
            log.info("Thread khác đang thực hiện flushMessages");
            return;
        }

        try {
            List<String> messageJsonList = stringRedisTemplate.opsForList().range(cacheKey, 0, -1);
            if (messageJsonList == null || messageJsonList.isEmpty()) {
                log.info("Không có tin nhắn nào để flush cho conversationId={}", conversationId);
                return;
            }

            List<Message> messages = messageJsonList.stream()
                    .map(msg -> messageMapper.toEntity(convertJSONToObject(msg, MessageResponse.class)))
                    .toList();

            messageRepository.saveAll(messages);
            stringRedisTemplate.delete(cacheKey);

            log.info("Đã flush {} tin nhắn vào DB cho conversationId={}", messages.size(), conversationId);
        } catch (Exception e) {
            log.error("Lỗi khi flush tin nhắn vào DB cho conversationId={}", conversationId);
        } finally {
            redissonLock.releaseLock(lockKey);
        }
    }

    private <T> T convertJSONToObject(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            log.error("Không thể chuyển đổi JSON sang MessageResponse");
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    private <T> String convertObjectToJson(T object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("Lỗi khi chuyển đổi thành JSON");
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }
}