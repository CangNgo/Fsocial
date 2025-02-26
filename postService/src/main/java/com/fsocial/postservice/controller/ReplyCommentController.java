package com.fsocial.postservice.controller;

import com.fsocial.postservice.dto.Response;
import com.fsocial.postservice.dto.replyComment.ReplyCommentRequest;
import com.fsocial.postservice.entity.ReplyComment;
import com.fsocial.postservice.exception.AppCheckedException;
import com.fsocial.postservice.mapper.ReplyCommentMapper;
import com.fsocial.postservice.services.impl.ReplyCommentServiceImpl;
import com.fsocial.postservice.services.impl.UploadImageImpl;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/comment/reply")
public class ReplyCommentController {

    ReplyCommentServiceImpl replyCommentService;

    @PostMapping
    public ResponseEntity<Response> replyComment(ReplyCommentRequest request) throws AppCheckedException, IOException {

        ReplyComment response = replyCommentService.addReplyComment(request);

        return ResponseEntity.ok().body(Response.builder()
                        .data(response)
                        .dateTime(LocalDateTime.now())
                        .message("Reply comment thành công")
                .build());

    }

}
