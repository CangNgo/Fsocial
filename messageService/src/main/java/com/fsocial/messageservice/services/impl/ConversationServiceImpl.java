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
import java.util.*;

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
        boolean flag = accountClient.validUserId(request.getReceiverId());
        if (!flag) {
            log.warn("Không tìm thấy User: {}", request.getReceiverId());
            throw new AppException(ErrorCode.ACCOUNT_NOT_EXISTED);
        }

        List<String> participants = Arrays.asList(request.getSenderId(), request.getReceiverId());

        if (conversationRepository.existsByParticipants(participants)) {
            throw new AppException(ErrorCode.CONVERSATION_EXISTED);
        }

        Conversation conversation = conversationMapper.toEntity(request);
        conversation.setParticipants(participants);

        return conversationRepository.save(conversation);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ConversationCreateResponse responseToClient(ConversationRequest request) {
        validateUser(request.getReceiverId());
        Conversation conversation = createConversation(request);
        String receiverId = conversation.getParticipants().getLast();

        ConversationCreateResponse response = conversationMapper.toConversationCreateResponse(conversation);
        response.setReceiverId(receiverId);

        return response;
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
        List<Conversation> conversations = conversationRepository.findAllByParticipantsContaining(userId);
        if (conversations.isEmpty()) return List.of();

        // Lấy danh sách conversationId
        List<String> conversationIds = conversations.stream()
                .map(Conversation::getId)
                .toList();

        // Lấy lastMessage của mỗi cuộc trò chuyện
        Map<String, LastMessage> lastMessagesMap = messageService.findLastMessagesForConversations(conversationIds);

        return conversations.stream()
                .map(conversation -> buildConversationResponse(conversation, userId, lastMessagesMap))
                .sorted(Comparator
                        .comparing((ConversationResponse c) -> {
                            LastMessage lastMessage = c.getLastMessage();
                            return lastMessage != null && !lastMessage.isRead(); // Tin nhắn chưa đọc lên trước
                        }).reversed()
                        .thenComparing(c -> {
                            LastMessage lastMessage = c.getLastMessage();
                            return lastMessage != null ? lastMessage.getCreateAt() : LocalDateTime.MIN;
                        }, Comparator.reverseOrder())) // Tin nhắn mới nhất lên trước
                .toList();
    }

    private ConversationResponse buildConversationResponse(
            Conversation conversation, String userId, Map<String, LastMessage> lastMessagesMap) {

        var conversationResponse = conversationMapper.toConversationResponse(conversation);
        conversationResponse.setLastMessage(lastMessagesMap.get(conversation.getId()));

        // Xác định receiverId
        List<String> participants = conversation.getParticipants();
        String receiverId = Objects.equals(participants.getFirst(), userId) ? participants.getLast() : participants.getFirst();

        // Lấy thông tin người nhận từ ProfileClient
        ProfileResponse profileResponse;
        try {
            profileResponse = profileClient.getAccountProfileFromAnotherService(receiverId);
        } catch (Exception e) {
            log.error("Lỗi khi gọi ProfileService cho receiverId {}: {}", receiverId, e.getMessage());
            throw new AppException(ErrorCode.PROFILE_NOT_EXISTED);
        }

        conversationResponse.setReceiverId(receiverId);
        conversationResponse.setFirstName(profileResponse.getFirstName());
        conversationResponse.setLastName(profileResponse.getLastName());
        conversationResponse.setAvatar(profileResponse.getAvatar());

        return conversationResponse;
    }


    private void validateUser(String userId) {
        if (!accountClient.validUserId(userId)) {
            log.warn("Tài khoản {} không tồn tại.", userId);
            throw new AppException(ErrorCode.ACCOUNT_NOT_EXISTED);
        }
    }
}
