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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/*
* Khong nhan request 1 image
* */
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/create")
public class PostController {
    PostService postService;

    @PostMapping
    public ResponseEntity<Response> createPost(
            @RequestParam("text") String text,
            @RequestParam(value = "media", required = false) MultipartFile[] media,
            @RequestParam("userId") String userId) {
        PostDTORequest postDTO = PostDTORequest.builder()
                .text(text)
                .media(media)

                .build();
        try {
            PostDTO post = postService.createPost(postDTO, userId);

            return ResponseEntity.ok().body(Response.builder()
                .data(post)
                .message("Thêm mới bài viết thành công")
                .statusCode(StatusCode.CREATE_POST_SUCCESS.getCode())
                .build());
        } catch (RuntimeException | AppCheckedException e) {
            return ResponseEntity.ok().body(Response.builder()
                .data(null)
                .message(e.getMessage())
                .statusCode(StatusCode.CREATE_POST_FAILED.getCode())
                .build());
        }
    }

    @GetMapping
    public ResponseEntity<Response> getPosts() {
        try {
            List<Post> post = postService.getPosts();

            return ResponseEntity.ok().body(Response.builder()
                    .data(post)
                    .message("Thêm mới bài viết thành công")
                    .statusCode(StatusCode.CREATE_POST_SUCCESS.getCode())
                    .build());
        } catch (RuntimeException e) {
            return ResponseEntity.ok().body(Response.builder()
                    .data(null)
                    .message(e.getMessage())
                    .statusCode(StatusCode.CREATE_POST_FAILED.getCode())
                    .build());
        }
    }
}
