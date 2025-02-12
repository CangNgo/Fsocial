package com.fsocial.timelineservice.controller;

import com.fsocial.timelineservice.dto.Response;
import com.fsocial.timelineservice.dto.comment.CommentResponse;
import com.fsocial.timelineservice.entity.Comment;
import com.fsocial.timelineservice.exception.StatusCode;
import com.fsocial.timelineservice.services.CommentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/comment")
public class CommentController {

    CommentService commentService;

    @GetMapping
    public ResponseEntity<Response> getComment(@RequestParam("postId") String postId) {
        try {
            List<CommentResponse> commentByPostId = commentService.getComments(postId);
            new Response();
            return ResponseEntity.ok(Response.builder()
                    .statusCode(StatusCode.GET_COMMENT_SUCCESS.getCode())
                    .data(commentByPostId)
                    .dateTime(LocalDateTime.now())
                    .message("Comment get by postId successfully")
                    .build());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
