package com.fsocial.messageservice.controller;

import com.fsocial.messageservice.dto.ApiResponse;
import com.fsocial.messageservice.dto.request.MessageRequest;
import com.fsocial.messageservice.dto.response.MessageResponse;
import com.fsocial.messageservice.enums.ResponseStatus;
import com.fsocial.messageservice.services.MessageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/chat")
public class MessageController {
    MessageService chatService;

    @GetMapping("/{conversationId}")
    public ApiResponse<List<MessageResponse>> getMessagesBetweenUsers(
            @PathVariable String conversationId,
            @RequestParam(defaultValue = "0") int page) {
        List<MessageResponse> messageResponses = chatService.findChatMessagesBetweenUsers(conversationId, page);
        return ApiResponse.buildApiResponse(messageResponses, ResponseStatus.SUCCESS);
    }

    @PostMapping("/create")
    public ApiResponse<MessageResponse> createMessage(@RequestBody MessageRequest request) {
        MessageResponse response = chatService.saveChatMessage(request);
        return ApiResponse.buildApiResponse(response, ResponseStatus.SUCCESS);
    }

    @PutMapping("/mark-read")
    public ApiResponse<?> markMessagesAsRead(@RequestParam String conversationId) {
        chatService.markMessagesAsRead(conversationId);
        return ApiResponse.buildApiResponse(null, ResponseStatus.SUCCESS);
    }


    @DeleteMapping("/{messageId}")
    public ApiResponse<Void> deleteMessage(@PathVariable String messageId) {
        chatService.deleteMessage(messageId);
        return ApiResponse.buildApiResponse(null, ResponseStatus.SUCCESS);
    }
}
