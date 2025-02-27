package com.fsocial.messageservice.Controller;
import com.fsocial.messageservice.Entity.ChatMessage;
import com.fsocial.messageservice.Enum.MessageType;
import com.fsocial.messageservice.services.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ChatWebSocketController {

    @Autowired
    private ChatService chatService;

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
}