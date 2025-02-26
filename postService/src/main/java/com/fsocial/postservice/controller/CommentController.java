package com.fsocial.postservice.controller;

import com.fsocial.postservice.dto.ApiResponse;
import com.fsocial.postservice.dto.comment.CommentDTORequest;
import com.fsocial.postservice.entity.Comment;
import com.fsocial.postservice.enums.ResponseStatus;
import com.fsocial.postservice.exception.AppCheckedException;
import com.fsocial.postservice.enums.ErrorCode;
import com.fsocial.postservice.services.CommentService;
import com.fsocial.postservice.services.UploadImage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/comment")
@Slf4j
public class CommentController {
    CommentService commentService;
    UploadImage uploadImage;

    @PostMapping
    public ApiResponse<Comment> createComment(@RequestBody CommentDTORequest request) {

        Comment comment = commentService.addComment(request);
        return ApiResponse.<Comment>builder()
                .statusCode(ResponseStatus.SUCCESS.getCODE())
                .message(ResponseStatus.SUCCESS.getMessage())
                .dateTime(LocalDateTime.now())
                .data(comment)
                .build();
    }
}
