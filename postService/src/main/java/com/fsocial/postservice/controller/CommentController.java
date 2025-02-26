package com.fsocial.postservice.controller;

import com.fsocial.postservice.dto.Response;
import com.fsocial.postservice.dto.comment.CommentDTORequest;
import com.fsocial.postservice.entity.Comment;
import com.fsocial.postservice.exception.AppCheckedException;
import com.fsocial.postservice.exception.StatusCode;
import com.fsocial.postservice.services.CommentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/comment")
public class CommentController {

    CommentService commentService;


    @PostMapping
    public ResponseEntity<Response> createComment(CommentDTORequest request) throws AppCheckedException {
        try {
            Comment comment = commentService.addComment(request);
            return ResponseEntity.ok(Response.builder()
                    .data(comment)
                    .dateTime(LocalDateTime.now())
                    .message("Comment created successfully")
                    .build());
        } catch (Exception e) {
            throw new AppCheckedException("Không thể thêm comment vào bài viết {}", StatusCode.CREATE_COMMENT_FAILED);
        }
    }

}
