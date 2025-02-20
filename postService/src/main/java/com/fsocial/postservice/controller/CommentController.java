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
            String[] uripostImage = new String[0];
            if(request.getMedia() != null && request.getMedia().length > 0) {
                MultipartFile[] validMedia = Arrays.stream(request.getMedia())
                        .filter(file -> file != null &&
                                !file.isEmpty() &&
                                file.getOriginalFilename() != null &&
                                !file.getOriginalFilename().isEmpty())
                        .toArray(MultipartFile[]::new);

                if (validMedia.length > 0) {
                    uripostImage = uploadImage.uploadImage(validMedia);
                }
            };

            Comment commentRequest = Comment.builder()
                    .countReplyComment(0)
                    .countLikes(0)
                    .reply(false)
                    .postId(request.getPostId())
                    .userId(request.getUserId())
                    .content(
                            Content.builder()
                                    .text(request.getText())
                                    .media(uripostImage)
                                    .HTMLText(request.getHTMLText())
                                    .build()
                    )
                    .build();
            Comment comment = commentService.addComment(commentRequest);
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
