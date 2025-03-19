package com.fsocial.messageservice.controller;

import com.fsocial.messageservice.dto.ApiResponse;
import com.fsocial.messageservice.dto.request.MessageRequest;
import com.fsocial.messageservice.dto.request.TypingStatusRequest;
import com.fsocial.messageservice.dto.response.MessageResponse;
import com.fsocial.messageservice.enums.ResponseStatus;
import com.fsocial.messageservice.services.MessageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatController {
    MessageService chatService;
    SimpMessagingTemplate messagingTemplate;

    /**
     * Nhận tin nhắn chat từ client gửi tới "/chat.sendMessage",
     * lưu vào MongoDB và broadcast tới topic "/topic/public".
     */
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public MessageResponse sendMessage(@Payload MessageRequest chatMessage) {
        return chatService.saveChatMessage(chatMessage);
    }

    /**
     * Khi có người dùng tham gia chat, nhận thông báo từ "/chat.addUser"
     * và gửi broadcast tới "/topic/public".
     */
//    @MessageMapping("/chat.addUser")
//    @SendTo("/topic/public")
//    public MessageResponse addUser(@Payload MessageRequest chatMessage,
//                               SimpMessageHeaderAccessor headerAccessor) {
//        // Lưu tên người dùng vào session
//        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
//        chatMessage.setType(MessageType.JOIN);
//        return chatMessage;
//    }

    /**
     * Nhận tin nhắn chat private từ client gửi tới "/chat.private".
     * Tin nhắn sẽ được lưu vào MongoDB và gửi tới user nhận thông qua "/user/queue/private".
     * Client nhận tin nhắn private cần subscribe tới "/user/queue/private".
     */
    @MessageMapping("/chat.private")
    public void sendPrivateMessage(@Payload MessageRequest request) {
        MessageResponse response = chatService.saveChatMessage(request);

        messagingTemplate.convertAndSend(
                "/queue/private-" + response.getReceiverId(),
                response
        );

        messagingTemplate.convertAndSend(
                "/queue/private-" + response.getConversationId(),
                response
        );
    }

    @MessageMapping("/chat.typing")
    public void sendTypingStatus(@Payload TypingStatusRequest request) {
        messagingTemplate.convertAndSend(
                "/queue/typing-" + request.getConversationId(),
                request
        );
    }

}
