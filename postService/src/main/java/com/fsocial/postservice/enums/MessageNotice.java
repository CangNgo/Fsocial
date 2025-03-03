package com.fsocial.postservice.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum MessageNotice {
    NOTIFICATION_COMMENT("notice-comment","đã bình luận bài viết của bạn."),
    NOTIFICATION_LIKE("notice-like","đã thích bài viết của bạn."),
    NOTIFICATION_REPLY_COMMENT("notice-reply-comment","đã phản hồi bình luận bài viết của bạn."),
    NOTIFICATION_LIKE_COMMENT("notice-like-comment", "Đã thích bình luận của bạn"),
    ;
    final String topic;
    final String message;
}
