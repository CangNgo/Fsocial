package com.fsocial.postservice.controller;

import com.fsocial.postservice.dto.PostDTO;
import com.fsocial.postservice.dto.Response;
import com.fsocial.postservice.entity.Post;
import com.fsocial.postservice.exception.AppCheckedException;
import com.fsocial.postservice.exception.StatusCode;
import com.fsocial.postservice.services.PostService;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostController {
    PostService postService;

    @PostMapping("/")
    public ResponseEntity<Response> createPost(@RequestBody PostDTO postDTO) {

        try {
            PostDTO post = postService.createPost(postDTO);

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
}
