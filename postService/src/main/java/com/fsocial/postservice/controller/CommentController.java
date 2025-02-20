package com.fsocial.postservice.controller;

import com.fsocial.postservice.dto.Response;
import com.fsocial.postservice.dto.comment.CommentDTORequest;
import com.fsocial.postservice.entity.Comment;
import com.fsocial.postservice.entity.Content;
import com.fsocial.postservice.exception.AppCheckedException;
import com.fsocial.postservice.exception.StatusCode;
import com.fsocial.postservice.services.CommentService;
import com.fsocial.postservice.services.UploadImage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.cglib.core.Local;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/comment")
public class CommentController {

    CommentService commentService;

    UploadImage uploadImage;

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
