package com.fsocial.messageservice.controller;

import com.fsocial.messageservice.dto.ApiResponse;
import com.fsocial.messageservice.dto.request.ConversationRequest;
import com.fsocial.messageservice.dto.response.ConversationCreateResponse;
import com.fsocial.messageservice.dto.response.ConversationResponse;
import com.fsocial.messageservice.enums.ResponseStatus;
import com.fsocial.messageservice.services.ConversationService;
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
@RequestMapping("/conversations")
@Slf4j
public class ConversationController {
    ConversationService conversationService;

    @PostMapping
    public ApiResponse<ConversationCreateResponse> createConversation(@RequestBody @Valid ConversationRequest request) {
        ConversationCreateResponse response = conversationService.responseToClient(request);
        return ApiResponse.buildApiResponse(response, ResponseStatus.SUCCESS);
    }

    @DeleteMapping("/{conversationId}")
    public ApiResponse<Void> deleteConversation(@PathVariable String conversationId) {
        conversationService.deleteConversation(conversationId);
        return ApiResponse.buildApiResponse(null, ResponseStatus.SUCCESS);
    }

    @GetMapping("/user/{userId}")
    public ApiResponse<List<ConversationResponse>> getUserConversations(@PathVariable String userId) {
        List<ConversationResponse> response = conversationService.getAllConversations(userId);
        return ApiResponse.buildApiResponse(response, ResponseStatus.SUCCESS);
    }
}
