package com.fsocial.messageservice.services;

import com.fsocial.messageservice.Entity.ChatMessage;
import com.fsocial.messageservice.Repository.ChatMessageRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.gridfs.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
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
        return chatMessageRepository.save(message);
    }

    public List<ChatMessage> findMessagesByUser(String username) {
        return chatMessageRepository.findBySenderOrReciver(username, username);
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
}