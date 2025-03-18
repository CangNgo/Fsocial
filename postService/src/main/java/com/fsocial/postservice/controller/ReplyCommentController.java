package com.fsocial.postservice.controller;

import com.fsocial.postservice.dto.Response;
import com.fsocial.postservice.dto.replyComment.ReplyCommentRequest;
import com.fsocial.postservice.dto.replyComment.ReplyCommentUpdateDTORequest;
import com.fsocial.postservice.entity.Comment;
import com.fsocial.postservice.entity.ReplyComment;
import com.fsocial.postservice.exception.AppCheckedException;
import com.fsocial.postservice.services.impl.ReplyCommentServiceImpl;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/comment/reply")
public class ReplyCommentController {

    ReplyCommentServiceImpl replyCommentService;

    @PostMapping
    public ResponseEntity<Response> replyComment(ReplyCommentRequest request) throws AppCheckedException {

        ReplyComment response = replyCommentService.addReplyComment(request);

        return ResponseEntity.ok().body(Response.builder()
                        .data(response)
                        .dateTime(LocalDateTime.now())
                        .message("Reply comment thành công")
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteReplyComment(@PathVariable("id") String id) throws AppCheckedException {
        return ResponseEntity.ok().body(Response.builder()
                .data(replyCommentService.deleteReplyComment(id))
                .message("Delete reply comment successfully")
                .build());
    }

    @PutMapping
    public ResponseEntity<Response> updateReplyComment(ReplyCommentUpdateDTORequest request) throws AppCheckedException {
        ReplyComment update = replyCommentService.updateReplyComment(request);
        return ResponseEntity.ok().body(Response.builder()
                .data(update)
                .message("Update reply comment successfully")
                .build());
    }
}
