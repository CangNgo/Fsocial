package com.fsocial.messageservice.controller;

import com.fsocial.messageservice.dto.ApiResponse;
import com.fsocial.messageservice.dto.request.MessageRequest;
import com.fsocial.messageservice.dto.response.MessageResponse;
import com.fsocial.messageservice.enums.ResponseStatus;
import com.fsocial.messageservice.services.MessageService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/messages")
@Slf4j
public class MessageController {
    MessageService messageService;

    @GetMapping("/{conversationId}")
    public ApiResponse<List<MessageResponse>> getMessages(
            @PathVariable String conversationId,
            @RequestParam(defaultValue = "0") int page) {
        List<MessageResponse> messages = messageService.findChatMessagesBetweenUsers(conversationId, page);
        return ApiResponse.buildApiResponse(messages, ResponseStatus.SUCCESS);
    }

    @PostMapping
    public ApiResponse<MessageResponse> sendMessage(@RequestBody @Valid MessageRequest request) {
        MessageResponse response = messageService.saveChatMessage(request);
        return ApiResponse.buildApiResponse(response, ResponseStatus.SUCCESS);
    }

    @DeleteMapping("/{messageId}")
    public ApiResponse<Void> deleteMessage(@PathVariable String messageId) {
        messageService.deleteMessage(messageId);
        return ApiResponse.buildApiResponse(null, ResponseStatus.SUCCESS);
    }

    @PutMapping("/{conversationId}/mark-as-read")
    public ApiResponse<Void> markMessagesAsRead(@PathVariable String conversationId) {
        messageService.markMessagesAsRead(conversationId);
        return ApiResponse.buildApiResponse(null, ResponseStatus.SUCCESS);
    }
}
