package com.fsocial.postservice.controller;

import com.fsocial.postservice.dto.Response;
import com.fsocial.postservice.dto.comment.CommentDTORequest;
import com.fsocial.postservice.dto.comment.LikeCommentDTO;
import com.fsocial.postservice.entity.Comment;
import com.fsocial.postservice.exception.AppCheckedException;
import com.fsocial.postservice.exception.StatusCode;
import com.fsocial.postservice.services.CommentService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/comment")
@Slf4j
public class CommentController {

    CommentService commentService;

    @PostMapping
    public ResponseEntity<Response> createComment(@RequestBody CommentDTORequest request) throws AppCheckedException{
            Comment comment = commentService.addComment(request);
            return ResponseEntity.ok(Response.builder()
                    .data(comment)
                    .message("Comment created successfully")
                    .build());
    }

    @PostMapping("/like")
    public ResponseEntity<Response> likeComment(@RequestBody @Valid LikeCommentDTO dto ) throws AppCheckedException {
        boolean like = commentService.toggleLikeComment(dto.getCommentId(), dto.getUserId());
        Map<String, Object> result = new HashMap<>();
        result.put("like", like);
        result.put("userid", dto.getUserId());
        return ResponseEntity.ok(Response.builder()
                .data(result)
                .message(like?"Thích bình luận thành công":"Hủy thích bình luận thành công")
                .build());
    }
}
