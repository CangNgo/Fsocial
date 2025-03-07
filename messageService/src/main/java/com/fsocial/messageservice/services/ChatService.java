package com.fsocial.messageservice.services;

import com.fsocial.messageservice.Entity.ChatMessage;
import com.fsocial.messageservice.Repository.ChatMessageRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.gridfs.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class ChatService {

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private GridFsOperations gridFsOperations;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    public List<ChatMessage> findAll() {
        return chatMessageRepository.findAll();
    }
    /**
     * Lưu các file đính kèm lên GridFS và trả về danh sách id của file.
     */
    private String createConversationId(String sender, String receiver) {
        return sender.compareTo(receiver) < 0
                ? sender + "_" + receiver
                : receiver + "_" + sender;
    }
    public List<ChatMessage> findChatMessagesBetweenUsers(String user1, String user2, int page) {
        String conversationId = createConversationId(user1, user2);
        Pageable pageable = PageRequest.of(page, 20); // Lấy 20 tin nhắn mỗi trang (mới nhất trước)

        Page<ChatMessage> chatPage = chatMessageRepository.findByConversationIdOrderByTimestampDesc(conversationId, pageable);

        return chatPage.getContent();
    }

    public List<String> storeFiles(MultipartFile[] files) {
        List<String> attachmentIds = new ArrayList<>();
        if (files != null) {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    try {
                        ObjectId fileId = gridFsTemplate.store(file.getInputStream(),
                                file.getOriginalFilename(), file.getContentType());
                        attachmentIds.add(fileId.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return attachmentIds;
    }

    /**
     * Lưu tin nhắn chat vào MongoDB.
     */
    public ChatMessage saveChatMessage(ChatMessage message) {
        if (message.getTimestamp() == null) {
            message.setTimestamp(new Date());
        }
        message.setConversationId(createConversationId(message.getSender(), message.getReceiver()));
        return chatMessageRepository.save(message);
    }

    public List<ChatMessage> findMessagesByUser(String userID) {
        return chatMessageRepository.findBySenderOrReceiver(userID, userID);
    }
    /**
     * Lấy file đính kèm từ GridFS theo id.
     */
    public Resource getFile(String id) {
        var gridFSFile = gridFsTemplate.findOne(query(where("_id").is(id)));
        if (gridFSFile == null) {
            return null;
        }
        return gridFsOperations.getResource(gridFSFile);
    }
    public void markMessageAsRead(String messageId) {
        ChatMessage message = chatMessageRepository.findById(messageId).orElse(null);
        if (message != null) {
            message.setRead(true);
            chatMessageRepository.save(message);
        }
    }
}
