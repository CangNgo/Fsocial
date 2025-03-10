package com.fsocial.messageservice.services.impl;

import com.fsocial.messageservice.Entity.Conversation;
import com.fsocial.messageservice.dto.request.ConversationRequest;
import com.fsocial.messageservice.dto.response.*;
import com.fsocial.messageservice.enums.ErrorCode;
import com.fsocial.messageservice.exception.AppException;
import com.fsocial.messageservice.mapper.ConversationMapper;
import com.fsocial.messageservice.repository.ConversationRepository;
import com.fsocial.messageservice.repository.httpClient.AccountClient;
import com.fsocial.messageservice.repository.httpClient.ProfileClient;
import com.fsocial.messageservice.services.MessageService;
import com.fsocial.messageservice.services.ConversationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ConversationServiceImpl implements ConversationService {
    ConversationRepository conversationRepository;
    ConversationMapper conversationMapper;
    MessageService messageService;
    ProfileClient profileClient;
    AccountClient accountClient;

    @Override
    public Conversation createConversation(ConversationRequest request) {
        ProfileResponse recipientProfile = Optional.ofNullable(profileClient.getAccountProfileFromAnotherService(request.getReceiverId()))
                .orElseThrow(() -> new AppException(ErrorCode.PROFILE_NOT_EXISTED));

        Conversation conversation = conversationMapper.toEntity(request);
        updateConversationWithProfile(conversation, recipientProfile);

        return conversationRepository.save(conversation);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ConversationCreateResponse responseToClient(ConversationRequest request) {
        validateUser(request.getReceiverId());

        // Kiểm tra nhanh xem cuộc trò chuyện đã tồn tại chưa
        if (conversationRepository.existsBySenderIdAndReceiverId(request.getSenderId(), request.getReceiverId())) {
            throw new AppException(ErrorCode.CONVERSATION_EXISTED);
        }

        Conversation conversation = createConversation(request);
        log.info("Khởi tạo thành công cuộc trò chuyện giữa {} và {}", request.getSenderId(), request.getReceiverId());
        return conversationMapper.toConversationCreateResponse(conversation);
    }

    @Override
    @Transactional
    public void deleteConversation(String conversationId) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new AppException(ErrorCode.CONVERSATION_NOT_EXIST));

        messageService.deleteMessagesByConversationId(conversationId);
        conversationRepository.delete(conversation);
        log.info("Xóa thành công cuộc trò chuyện {}", conversationId);
    }

    @Override
    public List<ConversationResponse> getAllConversations(String userId) {
        List<Conversation> conversations = conversationRepository.findAllBySenderId(userId);
        if (conversations.isEmpty()) return List.of();

        List<String> conversationIds = conversations.stream()
                .map(Conversation::getId)
                .toList();

        Map<String, LastMessage> lastMessagesMap = messageService.findLastMessagesForConversations(conversationIds);

        return conversations.stream()
                .map(conversation -> {
                    var conversationResponse = conversationMapper.toConversationResponse(conversation);
                    conversationResponse.setLastMessage(lastMessagesMap.get(conversation.getId()));
                    return conversationResponse;
                })
                .sorted(Comparator.comparing((ConversationResponse c) -> {
                            LastMessage lastMessage = c.getLastMessage();
                            return lastMessage != null && lastMessage.isRead(); // Tin nhắn chưa đọc lên trước
                        }).reversed()
                        .thenComparing((ConversationResponse c) -> {
                            LastMessage lastMessage = c.getLastMessage();
                            return lastMessage != null ? lastMessage.getCreateAt() : LocalDateTime.MIN; // Tin nhắn mới nhất trước
                        }).reversed())
                .toList();
    }

    private void validateUser(String userId) {
        if (!accountClient.validUserId(userId)) {
            log.warn("Tài khoản {} không tồn tại.", userId);
            throw new AppException(ErrorCode.ACCOUNT_NOT_EXISTED);
        }
    }

    private void updateConversationWithProfile(Conversation conversation, ProfileResponse profile) {
        conversation.setFirstName(profile.getFirstName());
        conversation.setLastName(profile.getLastName());
        conversation.setAvatar(profile.getAvatar());
    }
}
