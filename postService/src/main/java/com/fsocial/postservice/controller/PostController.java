package com.fsocial.postservice.controller;

import com.fsocial.postservice.dto.ApiResponse;
import com.fsocial.postservice.dto.post.LikePostDTO;
import com.fsocial.postservice.dto.post.PostDTO;
import com.fsocial.postservice.dto.post.PostDTORequest;
import com.fsocial.postservice.enums.ResponseStatus;
import com.fsocial.postservice.exception.AppCheckedException;
import com.fsocial.postservice.services.PostService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/actions")
public class PostController {
    PostService postService;

    @PostMapping
    public ResponseEntity<ApiResponse> createPost(@RequestBody PostDTORequest request) {

        try {
            PostDTO post = postService.createPost(request);

            return ResponseEntity.ok().body(ApiResponse.builder()
                    .data(post)
                    .message("Tạo bài viết thành công")
                    .statusCode(200)
                    .dateTime(LocalDateTime.now())
                    .build());
        } catch (RuntimeException | AppCheckedException e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .data(null)
                    .message(e.getMessage())
                    .dateTime(LocalDateTime.now())
                    .build());
        }
    }

    @PutMapping
    public ResponseEntity<ApiResponse> updatePost(
            @RequestParam("text") String text,
            @RequestParam("HTMLText") String HTMLText,
            @RequestParam("postId") String postId) {
        PostDTORequest postDTO = PostDTORequest.builder()
                .text(text)
                .HTMLText(HTMLText)
                .build();
        try {
            PostDTO post = postService.updatePost(postDTO, postId);

            return ResponseEntity.ok().body(ApiResponse.builder()
                    .data(post)
                    .message("Cập nhật bài viết thành công")
                    .statusCode(200)
                    .dateTime(LocalDateTime.now())
                    .build());
        } catch (RuntimeException | AppCheckedException e) {
            return ResponseEntity.ok().body(ApiResponse.builder()
                    .data(null)
                    .message(e.getMessage())
                    .dateTime(LocalDateTime.now())
                    .build());
        }
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse> deletePost(
            @RequestParam("postId") String postId) {
        postService.deletePost(postId);
        return ResponseEntity.ok().body(ApiResponse.builder()
                .message("Xóa bài viết thành công")
                .dateTime(LocalDateTime.now())
                .statusCode(200)
                .build());
    }

    //Like Post

    @GetMapping("/like")
    public ResponseEntity<ApiResponse> likePost(@RequestBody LikePostDTO likeDTO){
        try {
            boolean like = postService.toggleLike(likeDTO);
            return ResponseEntity.ok().body(ApiResponse.builder()
                    .data(like)
                    .message("Cập nhật bài viết thành công")
                    .statusCode(200)
                    .dateTime(LocalDateTime.now())
                    .build());
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        } catch (AppCheckedException e) {
            throw new RuntimeException(e);
        }
    }
}
