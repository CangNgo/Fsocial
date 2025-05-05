package com.fsocial.messageservice.controller;

import com.fsocial.messageservice.dto.ApiResponse;
import com.fsocial.messageservice.dto.request.ActionsRequest;
import com.fsocial.messageservice.dto.request.MessageRequest;
import com.fsocial.messageservice.dto.response.MessageListResponse;
import com.fsocial.messageservice.dto.response.MessageResponse;
import com.fsocial.messageservice.enums.ResponseStatus;
import com.fsocial.messageservice.services.ChatService;
import com.fsocial.messageservice.services.MessageService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/messages")
@Slf4j
public class MessageController {
    MessageService messageService;
    ChatService chatService;

    @GetMapping("/{conversationId}")
    public ApiResponse<MessageListResponse> getMessages(
            @PathVariable String conversationId,
            @RequestParam(defaultValue = "0") int page) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        MessageListResponse messages = messageService.findChatMessagesBetweenUsers(conversationId, page, userId);
        return ApiResponse.buildApiResponse(messages, ResponseStatus.SUCCESS);
    }

    @PostMapping
    public ApiResponse<MessageResponse> sendMessage(@RequestBody @Valid MessageRequest request) {
        MessageResponse response = chatService.cacheChatMessage(request);
        return ApiResponse.buildApiResponse(response, ResponseStatus.SUCCESS);
    }

    @PostMapping("/actions")
    public ApiResponse<?> handleChatActions(@RequestBody @Valid ActionsRequest request) {
        return chatService.handleChatActions(request);
    }

    @DeleteMapping("/{messageId}")
    public ApiResponse<Void> deleteMessage(@PathVariable String messageId) {
        messageService.deleteMessage(messageId);
        return ApiResponse.buildApiResponse(null, ResponseStatus.SUCCESS);
    }

    @PutMapping("/{conversationId}/mark-as-read")
    public ApiResponse<Void> markMessagesAsRead(@PathVariable String conversationId) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        messageService.markMessagesAsRead(conversationId, userId);
        return ApiResponse.buildApiResponse(null, ResponseStatus.SUCCESS);
    }
}
