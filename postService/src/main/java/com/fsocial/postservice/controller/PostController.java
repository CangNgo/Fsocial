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

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostController {
    PostService postService;
    String userId = "ljljgjdfje23423lkjdasf";

    @PostMapping()
    public ResponseEntity<Response> createPost(@RequestParam("text") String text,
                                               @RequestParam("media") MultipartFile[] media) {
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
}
