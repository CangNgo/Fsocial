package com.fsocial.timelineservice.controller;

import com.fsocial.timelineservice.dto.Response;
import com.fsocial.timelineservice.dto.post.PostDTO;
import com.fsocial.timelineservice.dto.post.PostResponse;
import com.fsocial.timelineservice.dto.profile.ProfileResponse;
import com.fsocial.timelineservice.dto.profile.ProfileServiceResponse;
import com.fsocial.timelineservice.entity.Post;
import com.fsocial.timelineservice.exception.AppCheckedException;
import com.fsocial.timelineservice.exception.StatusCode;
import com.fsocial.timelineservice.services.PostService;
import com.fsocial.timelineservice.services.ProfleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
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
            throw new AppCheckedException(e.getMessage(), StatusCode.POST_INVALID);
        }
    }
}
