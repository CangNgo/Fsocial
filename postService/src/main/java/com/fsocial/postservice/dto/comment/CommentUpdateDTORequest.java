package com.fsocial.postservice.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentUpdateDTORequest {
    LocalDateTime createdAt = LocalDateTime.now();
    String commentId;
    String userId;
    String text;
    String HTMLText;
}
