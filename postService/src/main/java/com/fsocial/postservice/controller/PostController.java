package com.fsocial.postservice.controller;

import com.fsocial.postservice.dto.post.PostDTO;
import com.fsocial.postservice.dto.post.PostDTORequest;
import com.fsocial.postservice.dto.Response;
import com.fsocial.postservice.exception.AppCheckedException;
import com.fsocial.postservice.exception.StatusCode;
import com.fsocial.postservice.services.PostService;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

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
    public ResponseEntity<Response> createPost(PostDTORequest request) {

        try {
            PostDTO post = postService.createPost(request);

            return ResponseEntity.ok().body(Response.builder()
                    .data(post)
                    .message("Thêm mới bài viết thành công")
                    .dateTime(LocalDateTime.now())
                    .build());
        } catch (RuntimeException | AppCheckedException e) {
            return ResponseEntity.ok().body(Response.builder()
                    .data(null)
                    .message(e.getMessage())
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
                    .dateTime(LocalDateTime.now())
                    .build());
        } catch (RuntimeException | AppCheckedException e) {
            return ResponseEntity.ok().body(Response.builder()
                    .data(null)
                    .message(e.getMessage())
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
                .dateTime(LocalDateTime.now())
                .build());
    }
}
