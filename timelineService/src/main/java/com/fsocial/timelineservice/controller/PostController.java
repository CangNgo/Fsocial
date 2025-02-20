package com.fsocial.timelineservice.controller;

import com.fsocial.timelineservice.dto.Response;
import com.fsocial.timelineservice.dto.post.PostResponse;
import com.fsocial.timelineservice.exception.AppCheckedException;
import com.fsocial.timelineservice.exception.StatusCode;
import com.fsocial.timelineservice.services.PostService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/post")
public class PostController {

    PostService postService;

    @GetMapping
    public ResponseEntity<Response> getPosts() throws AppCheckedException {
        try {
            List<PostResponse> posts = postService.getPosts();

            return ResponseEntity.ok(Response.builder()
                            .message("Lấy bài đăng thành công")
                            .statusCode(StatusCode.OK.getCode())
                            .dateTime(LocalDateTime.now())
                            .data(posts)
                    .build());
        } catch (RuntimeException e) {
            return ResponseEntity.ok(Response.builder()
                            .message(e.getMessage())
                            .dateTime(LocalDateTime.now())
                            .data(null)
                    .build());
        }
    }

    @GetMapping("/find")
    public ResponseEntity<Response> findPost(@RequestParam("find_post") String findString) throws AppCheckedException {
        try {
            List<PostResponse> findByText = postService.findByText(findString);

            return ResponseEntity.ok(Response.builder()
                    .message("Lấy bài đăng thành công")
                    .dateTime(LocalDateTime.now())
                    .data(findByText)
                    .build());
        } catch (RuntimeException e) {
            return ResponseEntity.ok(Response.builder()
                            .message(e.getMessage())
                            .dateTime(LocalDateTime.now())
                            .data(null)
                    .build());
        }
    }
}
