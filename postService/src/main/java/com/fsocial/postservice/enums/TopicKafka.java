package com.fsocial.postservice.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum TopicKafka {
    TOPIC_LIKE("notice-like"),
    TOPIC_COMMENT("notice-comment")
    ;
    final String topic;

    TopicKafka(String topic) {
        this.topic = topic;
    }
}
