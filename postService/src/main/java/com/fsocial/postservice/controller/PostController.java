package com.fsocial.postservice.controller;

import com.fsocial.postservice.dto.Response;
import com.fsocial.postservice.dto.post.*;
import com.fsocial.postservice.entity.Post;
import com.fsocial.postservice.exception.AppCheckedException;
import com.fsocial.postservice.exception.StatusCode;
import com.fsocial.postservice.services.PostService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/actions")
@Tag(name = "Post controller")
public class PostController {
    PostService postService;
    Logger logger = LoggerFactory.getLogger(PostController.class);

    @PostAuthorize("hasAnyRole('ROLE_USER')")
    @PostMapping
    public ResponseEntity<Response> createPost(@Valid PostDTORequest request) throws AppCheckedException {
        //Bai viet phai co noi dung hoac hinh anh
        if ((request.getText() == null || request.getText().isEmpty())
            && (request.getMedia() == null || request.getMedia().length == 0)) {
            throw new AppCheckedException("Bài viết phải có nội dung, hình ảnh hoặc video", StatusCode.NOT_CONTENT);
        }

        PostDTO post = postService.createPost(request);
        logger.info("Đăng bài viết thành công");
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
    public ResponseEntity<Response> sharePost(@Valid PostShareDTORequest share) {
        PostDTO post = postService.sharePost(share);
        return ResponseEntity.ok(Response.builder()
            .data(post)
            .statusCode(StatusCode.OK.getCode())
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

    @GetMapping
    public ResponseEntity<Response> getPosts(@RequestParam(value = "userId") String userId) throws AppCheckedException {
        List<PostResponse> posts;

        posts = postService.getPostsByUserId(userId);
        logger.info("Lấy thông tin bài viết thành công");
        return ResponseEntity.ok(Response.builder()
            .message("Lấy bài đăng thành công")
            .dateTime(LocalDateTime.now())
            .data(posts)
            .build());
    }

    @GetMapping("/following")
    public ResponseEntity<Response> getPostsByFollowing(@RequestParam(value = "userId") String userId) throws AppCheckedException {
        List<PostResponse> posts;

        try {
            posts = postService.getPostByFollowing(userId);
            logger.info("Lấy thông tin bài viết theo following thành công");
            return ResponseEntity.ok(Response.builder()
                .message("Lấy bài đăng theo following thành công")
                .dateTime(LocalDateTime.now())
                .data(posts)
                .build());
        } catch (Exception e) {
            logger.info("Lấy thông tin bài viết theo following thất bại");
            return ResponseEntity.badRequest().body(Response.builder()
                .message("Lấy bài đăng theo following thất b")
                .dateTime(LocalDateTime.now())
                .build());
        }
    }

    @GetMapping("/find")
    public ResponseEntity<Response> findPost(@RequestParam("find_post") String findString,
                                             @RequestParam("user_id") String userId) throws AppCheckedException {
        List<PostResponse> findByText = postService.findByText(findString, userId);
        logger.info("Tìm kiếm bài đăng theo text thành công");
        return ResponseEntity.ok(Response.builder()
            .message("Lấy bài đăng thành công")
            .dateTime(LocalDateTime.now())
            .data(findByText)
            .build());

    }

    @GetMapping("/getpost_id")
    public ResponseEntity<Response> getPostId(@RequestParam("post_id") String postId, @RequestParam("user_id") String userId) throws AppCheckedException {
        PostResponse result = postService.getPostById(postId, userId);
        logger.info("Tìm kiếm bài đăng theo id thành công");
        return ResponseEntity.ok(Response.builder()
            .message("Lấy bài đăng thành công")
            .dateTime(LocalDateTime.now())
            .data(result)
            .build());
    }

//    @GetMapping("/redis/get_viewed/{user_id}")
//    public ResponseEntity<Response> getViewedPost(@PathVariable("user_id") String userId)  {
//
//        return ResponseEntity.ok(Response.builder()
//                .message("Lấy danh sach bai viet da xem")
//                .dateTime(LocalDateTime.now())
//                .data(postService.getListViewed(userId))
//                .build());
//    }

    //thống kê số lượng bài viết
    @GetMapping("/statistics_post_today")
    public ResponseEntity<Response> getPosttStatistics(@RequestParam("date_time") String dateTime) {
        LocalDate date = LocalDate.parse(dateTime);
        LocalDateTime startDate = date.atStartOfDay();
        LocalDateTime endDate = date.atTime(23, 59, 59);
        List<PostStatisticsDTO> result = postService.countStatisticsPostToday(startDate, endDate);
        logger.info("Lấy thông tin thống kê theo {} thành công", date);
        return ResponseEntity.ok().body(Response.builder()
            .data(result)
            .message("Lấy toàn bộ danh sách thống kê số lượng bài viết trong ngày " + date + "  thành công")
            .build());
    }

    @GetMapping("/statistics_post_start_end")
    public ResponseEntity<Response> getPostStatistics(@RequestParam("startDate") String startDateRe, @RequestParam("endDate") String endDateRe) {
        LocalDate start = LocalDate.parse(startDateRe);
        LocalDate end = LocalDate.parse(endDateRe);
        LocalDateTime startDate = start.atStartOfDay();
        LocalDateTime endDate = end.atTime(23, 59, 59);

        return ResponseEntity.ok().body(Response.builder()
            .data(postService.countStatisticsPostLongDay(startDate, endDate))
            .message("Lấy toàn bộ danh sách thống kê số lượng bài viết từ ngày " + startDate + " đến " + endDate + "  thành công")
            .build());
    }
}
