package com.fsocial.postservice.controller;

import com.fsocial.postservice.dto.Response;
import com.fsocial.postservice.dto.post.LikePostDTO;
import com.fsocial.postservice.dto.post.PostDTO;
import com.fsocial.postservice.dto.post.PostDTORequest;
import com.fsocial.postservice.exception.AppCheckedException;
import com.fsocial.postservice.services.PostService;
import jakarta.validation.Valid;
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
    public ResponseEntity<Response> createPost(@RequestBody @Valid PostDTORequest request) throws AppCheckedException {
            PostDTO post = postService.createPost(request);

            return ResponseEntity.ok().body(Response.builder()
                    .data(post)
                    .message("Thêm mới bài viết thành công")
                    .dateTime(LocalDateTime.now())
                    .build());
    }

    @PutMapping
    public ResponseEntity<Response> updatePost(
            @RequestParam("text") String text,
            @RequestParam("HTMLText") String HTMLText,
            @RequestParam("postId") String postId) throws AppCheckedException {
        PostDTORequest postDTO = PostDTORequest.builder()
                .text(text)
                .HTMLText(HTMLText)
                .build();
            PostDTO post = postService.updatePost(postDTO, postId);
            return ResponseEntity.ok().body(Response.builder()
                    .data(post)
                    .message("Cập nhật bài viết thành công")
                    .statusCode(200)
                    .dateTime(LocalDateTime.now())
                    .build());
    }

    @DeleteMapping
    public ResponseEntity<Response> deletePost(
            @RequestParam("postId") String postId) {
        postService.deletePost(postId);
        return ResponseEntity.ok().body(Response.builder()
                .message("Xóa bài viết thành công")
                .dateTime(LocalDateTime.now())
                .statusCode(200)
                .build());
    }

    //Like Post

    @GetMapping("/like")
    public ResponseEntity<Response> likePost(@RequestBody LikePostDTO likeDTO){
        try {
            boolean like = postService.toggleLike(likeDTO);
            return ResponseEntity.ok().body(Response.builder()
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
