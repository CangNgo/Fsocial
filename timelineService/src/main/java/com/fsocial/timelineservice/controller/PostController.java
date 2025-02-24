package com.fsocial.timelineservice.controller;

import com.fsocial.timelineservice.dto.ApiResponse;
import com.fsocial.timelineservice.dto.Response;
import com.fsocial.timelineservice.dto.post.PostResponse;
import com.fsocial.timelineservice.enums.ResponseStatus;
import com.fsocial.timelineservice.exception.AppCheckedException;
import com.fsocial.timelineservice.services.PostService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class PostController {

    PostService postService;

    @GetMapping
    public ApiResponse<List<PostResponse>> getPosts() throws AppCheckedException {
        List<PostResponse> posts = postService.getPosts();
        log.info("Lấy bài đăng thành công");
        return ApiResponse.<List<PostResponse>>builder()
                .statusCode(ResponseStatus.SUCCESS.getCODE())
                .message(ResponseStatus.SUCCESS.getMessage())
                .dateTime(LocalDateTime.now())
                .data(posts)
                .build();

    }

    @GetMapping("/find")
    public ApiResponse<List<PostResponse>> findPost(@RequestParam("find_post") String findString) throws AppCheckedException {
        List<PostResponse> findByText = postService.findByText(findString);
        return ApiResponse.<List<PostResponse>>builder()
                .statusCode(ResponseStatus.SUCCESS.getCODE())
                .message(ResponseStatus.SUCCESS.getMessage())
                .dateTime(LocalDateTime.now())
                .data(findByText)
                .build();
    }
}
