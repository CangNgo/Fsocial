package com.fsocial.postservice.controller;

import com.fsocial.postservice.dto.PostDTO;
import com.fsocial.postservice.dto.PostDTORequest;
import com.fsocial.postservice.dto.Response;
import com.fsocial.postservice.entity.Post;
import com.fsocial.postservice.exception.AppCheckedException;
import com.fsocial.postservice.exception.StatusCode;
import com.fsocial.postservice.services.PostService;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.cglib.core.Local;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

/*
 * Khong nhan request 1 image
 * */
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/actions")
public class PostController {
    PostService postService;

    @PostMapping
    public ResponseEntity<Response> createPost(
            @RequestParam("text") String text,
            @RequestParam("HTMLText") String HTMLText,
            @RequestParam(value = "media", required = false) MultipartFile[] media,
            @RequestParam("userId") String userId) {
        PostDTORequest postDTO = PostDTORequest.builder()
                .text(text)
                .HTMLText(HTMLText)
                .media(media)
                .build();
        try {
            PostDTO post = postService.createPost(postDTO, userId);

            return ResponseEntity.ok().body(Response.builder()
                    .data(post)
                    .message("Thêm mới bài viết thành công")
                    .statusCode(StatusCode.CREATE_POST_SUCCESS.getCode())
                    .dateTime(LocalDateTime.now())
                    .build());
        } catch (RuntimeException | AppCheckedException e) {
            return ResponseEntity.ok().body(Response.builder()
                    .data(null)
                    .message(e.getMessage())
                    .statusCode(StatusCode.CREATE_POST_FAILED.getCode())
                    .dateTime(LocalDateTime.now())
                    .build());
        }
    }

    @PutMapping
    public ResponseEntity<Response> updatePost(
            @RequestParam("text") String text,
            @RequestParam("HTMLText") String HTMLText,
            @RequestParam("postId") String postId) {
        PostDTORequest postDTO = PostDTORequest.builder()
                .text(text)
                .HTMLText(HTMLText)
                .build();
        try {
            PostDTO post = postService.updatePost(postDTO, postId);

            return ResponseEntity.ok().body(Response.builder()
                    .data(post)
                    .message("Cập nhật bài viết thành công")
                    .statusCode(StatusCode.CREATE_POST_SUCCESS.getCode())
                    .dateTime(LocalDateTime.now())
                    .build());
        } catch (RuntimeException | AppCheckedException e) {
            return ResponseEntity.ok().body(Response.builder()
                    .data(null)
                    .message(e.getMessage())
                    .statusCode(StatusCode.CREATE_POST_FAILED.getCode())
                    .dateTime(LocalDateTime.now())
                    .build());
        }
    }

    @DeleteMapping
    public ResponseEntity<Response> deletePost(
            @RequestParam("postId") String postId) {
        postService.deletePost(postId);
        return ResponseEntity.ok().body(Response.builder()
                .message("Xóa bài viết thành công")
                .statusCode(StatusCode.CREATE_POST_SUCCESS.getCode())
                .dateTime(LocalDateTime.now())
                .build());
    }
}
