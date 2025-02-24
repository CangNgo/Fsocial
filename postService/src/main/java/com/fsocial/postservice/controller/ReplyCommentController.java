package com.fsocial.postservice.controller;

import com.fsocial.postservice.dto.ApiResponse;
import com.fsocial.postservice.dto.replyComment.ReplyCommentRequest;
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
    public ResponseEntity<ApiResponse> replyComment(ReplyCommentRequest request) throws AppCheckedException, IOException {

        ReplyComment response = replyCommentService.addReplyComment(request);

        return ResponseEntity.ok().body(ApiResponse.builder()
                        .data(response)
                        .dateTime(LocalDateTime.now())
                        .message("Reply comment thành công")
                .build());

    }

}
