package com.fsocial.messageservice.Controller;

import com.fsocial.messageservice.Entity.ChatMessage;
import com.fsocial.messageservice.Enum.MessageType;
import com.fsocial.messageservice.services.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ChatWebSocketController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    /**
     * Nhận tin nhắn chat từ client gửi tới "/chat.sendMessage",
     * lưu vào MongoDB và broadcast tới topic "/topic/public".
     */
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        chatService.saveChatMessage(chatMessage);
        System.out.println("Đã chạy vào đây");
        return chatMessage;
    }

    @GetMapping("/allChat")
    @ResponseBody
    public List<ChatMessage> getAll() {
        return chatService.findAll();
    }

    /**
     * Khi có người dùng tham gia chat, nhận thông báo từ "/chat.addUser"
     * và gửi broadcast tới "/topic/public".
     */
    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage,
                               SimpMessageHeaderAccessor headerAccessor) {
        // Lưu tên người dùng vào session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        chatMessage.setType(MessageType.JOIN);
        return chatMessage;
    }

    /**
     * Nhận tin nhắn chat private từ client gửi tới "/chat.private".
     * Tin nhắn sẽ được lưu vào MongoDB và gửi tới user nhận thông qua "/user/queue/private".
     * Client nhận tin nhắn private cần subscribe tới "/user/queue/private".
     */
   @MessageMapping("/chat.private")
    public void sendPrivateMessage(@Payload ChatMessage chatMessage) {

        chatService.saveChatMessage(chatMessage);
        messagingTemplate.convertAndSend(
                "/queue/private-" + chatMessage.getReceiver(),
                chatMessage
        );
    }


    @GetMapping("/chat/messages/{userId}")
    @ResponseBody
    public List<ChatMessage> getMessagesByUser(@PathVariable String userId) {
        return chatService.findMessagesByUser(userId);
    }
    
    @PutMapping("/chat/messages/{messageId}/read") // api chuyển trạng thái tin nhắn
    @ResponseBody
    public void markMessageAsRead(@PathVariable String messageId) {
        chatService.markMessageAsRead(messageId);
    }
    
    @GetMapping("/chat/messages/{user1}/{user2}") // api hiển thị tin nhắn
    @ResponseBody
    public List<ChatMessage> getMessagesBetweenUsers(
            @PathVariable String user1,
            @PathVariable String user2,
            @RequestParam(defaultValue = "0") int page) {
        return chatService.findChatMessagesBetweenUsers(user1, user2, page);
    }
}
