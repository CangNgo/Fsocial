package com.fsocial.messageservice.controller;

import com.fsocial.messageservice.dto.ApiResponse;
import com.fsocial.messageservice.dto.request.MarkReadRequest;
import com.fsocial.messageservice.dto.request.MarkReadResponse;
import com.fsocial.messageservice.dto.request.MessageRequest;
import com.fsocial.messageservice.dto.request.TypingStatusRequest;
import com.fsocial.messageservice.dto.response.MessageResponse;
import com.fsocial.messageservice.enums.ResponseStatus;
import com.fsocial.messageservice.services.ChatService;
import com.fsocial.messageservice.services.MessageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatController {
    ChatService chatService;
    MessageService messageService;
    SimpMessagingTemplate messagingTemplate;

    /**
     * Nhận tin nhắn chat private từ client gửi tới "/chat.private".
     * Tin nhắn sẽ được lưu vào MongoDB và gửi tới user nhận thông qua "/user/queue/private".
     * Client nhận tin nhắn private cần subscribe tới "/user/queue/private".
     */
    @MessageMapping("/chat.private")
    public void sendPrivateMessage(@Payload MessageRequest request) {
        MessageResponse response = chatService.cacheChatMessage(request);

        messagingTemplate.convertAndSend(
                "/queue/private-" + response.getReceiverId(),
                response
        );

        messagingTemplate.convertAndSend(
                "/queue/private-" + response.getConversationId(),
                response
        );
    }

    @MessageMapping("/chat.read")
    public void markMessagesAsRead(@Payload MarkReadRequest request) {
        messageService.markMessagesAsRead(request.getConversationId(), request.getReaderId());

        MarkReadResponse response = MarkReadResponse.builder()
                .conversationId(request.getConversationId())
                .readerId(request.getReaderId())
                .build();

        // Gửi thông báo WebSocket đến tất cả user trong cuộc trò chuyện
        messagingTemplate.convertAndSend("/topic/chat.read." + request.getConversationId(), response);
    }

    @MessageMapping("/chat.typing")
    public void sendTypingStatus(@Payload TypingStatusRequest request) {
        messagingTemplate.convertAndSend(
                "/queue/typing-" + request.getConversationId(),
                request
        );
    }

}
