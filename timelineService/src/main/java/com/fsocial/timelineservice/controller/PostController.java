package com.fsocial.timelineservice.controller;

import com.fsocial.timelineservice.dto.Response;
import com.fsocial.timelineservice.dto.post.PostResponse;
import com.fsocial.timelineservice.dto.post.PostStatisticsDTO;
import com.fsocial.timelineservice.entity.Post;
import com.fsocial.timelineservice.exception.AppCheckedException;
import com.fsocial.timelineservice.services.PostService;
import com.fsocial.timelineservice.services.RedisService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/post")
public class PostController {

    PostService postService;

    Logger logger = LoggerFactory.getLogger(PostController.class);

    @GetMapping
    public ResponseEntity<Response> getPosts(@RequestParam(value = "userId") String userId ) throws AppCheckedException {
        List<PostResponse> posts ;

        posts = postService.getPostsByUserId(userId);
        logger.info("Lấy thông tin bài viết thành công");
        return ResponseEntity.ok(Response.builder()
                .message("Lấy bài đăng thành công")
                .dateTime(LocalDateTime.now())
                .data(posts)
                .build());
    }

    @GetMapping("/following")
    public ResponseEntity<Response> getPostsByFollowing(@RequestParam(value = "userId") String userId ) throws AppCheckedException {
        List<PostResponse> posts ;

//        if(userId == null) {
//             posts = postService.getPosts();
//        }else {
//        }

        try {
            posts = postService.getPostByFollowing(userId);
            logger.info("Lấy thông tin bài viết theo following thành công");
            return ResponseEntity.ok(Response.builder()
                    .message("Lấy bài đăng theo following thành công")
                    .dateTime(LocalDateTime.now())
                    .data(posts)
                    .build());
        }catch (Exception e) {
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
    public ResponseEntity<Response> getPostId(@RequestParam("post_id") String postId,@RequestParam("user_id") String userId) throws AppCheckedException {
        PostResponse result = postService.getPostById(postId,userId);
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
    public ResponseEntity<Response> getPosttStatistics(@RequestParam("date_time" )String dateTime) {
        LocalDate date = LocalDate.parse(dateTime);
        LocalDateTime startDate = date.atStartOfDay();
        LocalDateTime endDate = date.atTime(23, 59, 59);
        List<PostStatisticsDTO >result = postService.countStatisticsPostToday(startDate, endDate);
        logger.info("Lấy thông tin thống kê theo {} thành công", date);
        return ResponseEntity.ok().body(Response.builder()
                .data(result)
                .message("Lấy toàn bộ danh sách thống kê số lượng bài viết trong ngày " + date +  "  thành công")
                .build());
    }

    @GetMapping("/statistics_post_start_end")
    public ResponseEntity<Response> getPostStatistics(@RequestParam("startDate" )String startDateRe,@RequestParam("endDate" )String endDateRe ) {
        LocalDate start = LocalDate.parse(startDateRe);
        LocalDate end = LocalDate.parse(endDateRe);
        LocalDateTime startDate= start.atStartOfDay();
        LocalDateTime endDate= end.atTime(23, 59, 59);

        return ResponseEntity.ok().body(Response.builder()
                .data(postService.countStatisticsPostLongDay(startDate, endDate))
                .message("Lấy toàn bộ danh sách thống kê số lượng bài viết từ ngày " +startDate + " đến " + endDate + "  thành công")
                .build());
    }
}
