package com.fsocial.messageservice.Entity;

import com.fsocial.messageservice.Enum.MessageType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "chat_messages")
public class ChatMessage extends AbstractEntity<String>{
    @Id
    private String id;
    private MessageType type;
    private String sender;
    private String reciver;
    private String content;
    private Date timestamp;
    // Danh sách id của file đính kèm (lưu trên GridFS)
    private List<String> attachmentIds;

}
