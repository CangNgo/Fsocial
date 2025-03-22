package com.fsocial.messageservice.dto.request;

import com.fsocial.messageservice.enums.TypesAction;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ActionsRequest {
    TypesAction type;
    String conversationId;
    String senderId;
    Map<String, Object> extraProperties = new HashMap<>();

    public void addProperty(String key, Object value) {
        extraProperties.put(key, value);
    }

    public Object getProperty(String key) {
        return extraProperties.get(key);
    }

    public Map<String, Object> getAllProperties() {
        return extraProperties;
    }
}
