package com.fsocial.postservice.controller;

import com.fsocial.postservice.dto.Response;
import com.fsocial.postservice.dto.replyComment.LikeReplyCommentDTO;
import com.fsocial.postservice.dto.replyComment.ReplyCommentRequest;
import com.fsocial.postservice.dto.replyComment.ReplyCommentUpdateDTORequest;
import com.fsocial.postservice.entity.Comment;
import com.fsocial.postservice.entity.ReplyComment;
import com.fsocial.postservice.exception.AppCheckedException;
import com.fsocial.postservice.services.impl.ReplyCommentServiceImpl;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/comment/reply")
public class ReplyCommentController {

    ReplyCommentServiceImpl replyCommentService;

    @GetMapping("/like")
    public ResponseEntity<Response> likeReplyComment(@RequestBody @Valid LikeReplyCommentDTO request) throws AppCheckedException {
        boolean like = replyCommentService.likeReplyComment(request);
        Map<String, Boolean> map = new HashMap<>();
        map.put("like", like);
        return ResponseEntity.ok().body(Response.builder()
                .data(map)
                .dateTime(LocalDateTime.now())
                .message(like?"Like thành công":"Bỏ like thành công")
                .build());
    }

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
