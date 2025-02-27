package com.fsocial.messageservice.Entity;

import com.fsocial.messageservice.Enum.MessageType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "chat_messages")
public class ChatMessage extends AbstractEntity<String>{
    @Id
    private String id;
    private MessageType type;
    private String sender;
    private String content;
    private Date timestamp;
    // Danh sách id của file đính kèm (lưu trên GridFS)
    private List<String> attachmentIds;

    public ChatMessage() {
        this.timestamp = new Date();
    }

    public ChatMessage(MessageType type, String sender, String content, List<String> attachmentIds) {
        this.type = type;
        this.sender = sender;
        this.content = content;
        this.attachmentIds = attachmentIds;
        this.timestamp = new Date();
    }

    // Getters & Setters
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public MessageType getType() {
        return type;
    }
    public void setType(MessageType type) {
        this.type = type;
    }
    public String getSender() {
        return sender;
    }
    public void setSender(String sender) {
        this.sender = sender;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public Date getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
    public List<String> getAttachmentIds() {
        return attachmentIds;
    }
    public void setAttachmentIds(List<String> attachmentIds) {
        this.attachmentIds = attachmentIds;
    }
}
