package com.fsocial.notificationService.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.Arrays;
import java.util.List;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum TopicKafka {
    COMMENT("notice-comment"),
    LIKE("notice-like"),
    FOLLOW("notice-follow");

    final String topic;

    public static TopicKafka fromTopic(String topic) {
        return Arrays.stream(values())
                .filter(type -> type.getTopic().equals(topic))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown topic: " + topic));
    }

    public static List<String> getAllTopics() {
        return Arrays.stream(values())
                .map(TopicKafka::getTopic)
                .toList();
    }

    TopicKafka(String topic) {
        this.topic = topic;
    }
}
