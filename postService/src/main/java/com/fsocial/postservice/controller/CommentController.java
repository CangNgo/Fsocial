package com.fsocial.postservice.controller;

import com.fsocial.postservice.dto.ApiResponse;
import com.fsocial.postservice.dto.comment.CommentDTORequest;
import com.fsocial.postservice.entity.Comment;
import com.fsocial.postservice.exception.AppCheckedException;
import com.fsocial.postservice.enums.ErrorCode;
import com.fsocial.postservice.services.CommentService;
import com.fsocial.postservice.services.UploadImage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/comment")
public class CommentController {

    CommentService commentService;

    UploadImage uploadImage;


    @PostMapping
    public ResponseEntity<ApiResponse> createComment(CommentDTORequest request) throws AppCheckedException {
        try {
            Comment comment = commentService.addComment(request);
            return ResponseEntity.ok(ApiResponse.builder()
                    .data(comment)
                    .dateTime(LocalDateTime.now())
                    .message("Đã tạo bình luận thành công.")
                    .build());
        } catch (Exception e) {
            throw new AppCheckedException("Không thể thêm comment vào bài viết {}", ErrorCode.CREATE_COMMENT_FAILED);
        }
    }

}
