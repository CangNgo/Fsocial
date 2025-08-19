package com.fsocial.messageservice.controller;

import com.fsocial.messageservice.dto.ApiResponse;
import com.fsocial.messageservice.dto.request.ActionsRequest;
import com.fsocial.messageservice.dto.request.MessageRequest;
import com.fsocial.messageservice.dto.response.MessageResponse;
import com.fsocial.messageservice.services.ChatService;
import com.fsocial.messageservice.services.MessageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

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

    @MessageMapping("/chat.actions")
    public void markMessagesAsRead(@Payload ActionsRequest request) {
        ApiResponse<?> response = chatService.handleChatActions(request);

        // Gửi thông báo WebSocket đến tất cả user trong cuộc trò chuyện
        messagingTemplate.convertAndSend("/queue/actions-" + request.getConversationId(), response);
    }

}
