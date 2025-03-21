package com.fsocial.postservice.controller;

import com.fsocial.postservice.dto.Response;
import com.fsocial.postservice.dto.post.LikePostDTO;
import com.fsocial.postservice.dto.post.PostDTO;
import com.fsocial.postservice.dto.post.PostDTORequest;
import com.fsocial.postservice.dto.post.PostShareDTORequest;
import com.fsocial.postservice.entity.Post;
import com.fsocial.postservice.enums.ResponseStatus;
import com.fsocial.postservice.exception.AppCheckedException;
import com.fsocial.postservice.exception.StatusCode;
import com.fsocial.postservice.services.PostService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/actions")
@Tag(name ="Post controller")
public class PostController {
    PostService postService;

    @PostMapping
    public ResponseEntity<Response> createPost(@Valid PostDTORequest request) throws AppCheckedException {
        //If text and media is null -> return
        if ((request.getText() == null || request.getText().isEmpty())
                && (request.getMedia() == null || request.getMedia().length == 0)) {
            throw new AppCheckedException("Bài viết phải có nội dung, hình ảnh hoặc video", StatusCode.NOT_CONTENT);
        }

        PostDTO post = postService.createPost(request);

        return ResponseEntity.ok(Response.builder()
                .data(post)
                .statusCode(StatusCode.CREATE_POST_SUCCESS.getCode())
                .message("Tạo bài viết thành công")
                .build());
    }

    @PutMapping
    public ResponseEntity<Response> updatePost(
            @RequestParam("text") String text,
            @RequestParam("HTMLText") String HTMLText,
            @RequestParam("postId") String postId) throws AppCheckedException {

        //check postId
        if (postId == null || postId.isEmpty()) {
            throw new AppCheckedException("Mã bài viết không được để trống", StatusCode.POST_NOT_FOUND);
        }

        //Mapping DTO
        PostDTORequest postDTO = PostDTORequest.builder()
                .text(text)
                .HTMLText(HTMLText)
                .build();

        //Update Post
        PostDTO post = postService.updatePost(postDTO, postId);

        //return result
        return ResponseEntity.ok(Response.builder()
                .data(post)
                .message("Cập nhật bài viết thành công")
                .build());
    }

    @DeleteMapping
    public ResponseEntity<Response> deletePost(
            @RequestParam("postId") String postId) {
        postService.deletePost(postId);
        return ResponseEntity.ok(Response.builder()
                .message("Xóa bài viết thành công")
                .build());
    }

    //Like Post
    @PostMapping("/like")
    public ResponseEntity<Response> likePost(@RequestBody LikePostDTO likeDTO) throws Exception {
        boolean like = postService.toggleLike(likeDTO.getPostId(), likeDTO.getUserId());

        Map<String, Object> map = new HashMap<>();
        map.put("like", like);
        map.put("userId", likeDTO.getUserId());
        return ResponseEntity.ok(Response.builder()
                .data(map)
                .message(like ? "Thích bài viết thành công" : "bỏ thích bài viết thành công")
                .build());
    }
    @PostMapping("/share")
    public ResponseEntity<Response> sharePost(@Valid PostShareDTORequest share){
        PostDTO post = postService.sharePost(share);
        return ResponseEntity.ok(Response.builder()
                .data(post)
                .statusCode(StatusCode.CREATE_POST_SUCCESS.getCode())
                .message("Chia sẽ bài viết thành công")
                .build());
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Post>> getPostsByUser(@PathVariable String userId, @RequestParam String requesterId) {
        List<Post> posts = postService.getPostsByUser(userId, requesterId);
        if (posts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(posts);
    }
}
