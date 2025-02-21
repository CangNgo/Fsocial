package com.fsocial.timelineservice.controller;

import com.fsocial.timelineservice.dto.Response;
import com.fsocial.timelineservice.services.ReplyCommentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/comment/reply")
public class ReplyCommentController {

    ReplyCommentService replyCommentService;

    @GetMapping
    public ResponseEntity<Response> getCommentByCommentId(@RequestParam("comment_id") String commentId) {

        return ResponseEntity.ok().body(Response.builder()
                        .data(replyCommentService.getReplyCommentsByCommentId(commentId))
                        .dateTime(LocalDateTime.now())
                        .statusCode(200)
                        .message("Lấy thông tin trả lời comment thành công")
                .build());
    }
}
