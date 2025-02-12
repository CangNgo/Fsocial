package com.fsocial.postservice.controller;

import com.fsocial.postservice.dto.Response;
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
import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/comment")
public class CommentController {

    CommentService commentService;

    UploadImage uploadImage;

    @PostMapping
    public ResponseEntity<Response> createComment(@RequestParam("postId") String postId,
                                                  @RequestParam(value = "userId") String userId,
                                                  @RequestParam("text") String text,
                                                  @RequestParam(value = "media", required = false) MultipartFile[] media) throws AppCheckedException {
        try {
            String[] mediaText = uploadImage.uploadImage(media);

            Comment commentRequest = Comment.builder()
                    .countReplyComment(0)
                    .countLikes(0)
                    .reply(false)
                    .postId(postId)
                    .userId(userId)
                    .content(
                            Content.builder()
                                    .text(text)
                                    .media(mediaText)
                                    .build()
                    )
                    .build();
            Comment comment = commentService.addComment(commentRequest);
            new Response();
            return ResponseEntity.ok(Response.builder()
                    .statusCode(StatusCode.CREATE_COMMENT_SUCCESS.getCode())
                    .data(comment)
                    .dateTime(LocalDateTime.now())
                    .message("Comment created successfully")
                    .build());
        } catch (Exception e) {
            throw new AppCheckedException("Không thể thêm comment vào bài viết {}", StatusCode.CREATE_COMMENT_FAILED);
        }
    }

    @GetMapping
    public ResponseEntity<Response> getComment(@RequestParam("postId") String postId) {
        try {
            List<Comment> commentByPostId = commentService.getComments(postId);
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
