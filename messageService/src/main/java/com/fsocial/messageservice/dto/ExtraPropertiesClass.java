package com.fsocial.messageservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ExtraPropertiesClass {
    public Map<String, Object> extraProperties = new HashMap<>();

    public void addProperty(String key, Object value) {
        extraProperties.put(key, value);
    }

    public void removeProperty(String key) {
        extraProperties.remove(key);
    }

    public Object getProperty(String key) {
        return extraProperties.get(key);
    }

    @JsonIgnore
    public Map<String, Object> getAllProperties() {
        return extraProperties;
    }
}
