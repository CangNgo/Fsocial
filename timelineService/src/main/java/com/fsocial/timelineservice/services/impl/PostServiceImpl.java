package com.fsocial.timelineservice.services.impl;

import com.fsocial.timelineservice.dto.post.PostResponse;
import com.fsocial.timelineservice.dto.post.PostStatisticsDTO;
import com.fsocial.timelineservice.dto.post.PostStatisticsLongDateDTO;
import com.fsocial.timelineservice.dto.profile.ProfileResponse;
import com.fsocial.timelineservice.entity.Post;
import com.fsocial.timelineservice.enums.StatusCode;
import com.fsocial.timelineservice.exception.AppCheckedException;
import com.fsocial.timelineservice.exception.AppUnCheckedException;
import com.fsocial.timelineservice.repository.CommentRepository;
import com.fsocial.timelineservice.repository.PostRepository;
import com.fsocial.timelineservice.repository.httpClient.ProfileClient;
import com.fsocial.timelineservice.services.PostService;
import com.fsocial.timelineservice.services.RedisService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostServiceImpl implements PostService {

    PostRepository postRepository;

    ProfileClient profileClient;

    CommentRepository commentRepository;

    RedisService redisService;

    static Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);

//    @Override
//    public List<PostResponse> getPosts() throws AppUnCheckedException {
//        List<String> viewed = getListViewed(userId);
//        return postRepository.findByIdNotInOrOrderByCreateDatetimeDesc(viewed, pageable).stream()
//                .map(post -> {
//                    try {
//                        return this.mapToPostResponse(post);
//                    } catch (AppCheckedException e) {
//                        throw new RuntimeException(e.getMessage());
//                    }
//                })
//                .collect(Collectors.toList());
//    }

    @Override
    public List<PostResponse> getPostsByUserId(String userId) throws AppUnCheckedException {
        Pageable pageable = PageRequest.of(0, 10);
        List<String> viewed = getListViewed(userId);
        List<String> personalization = getPersonalization(userId);
        for (String personal : personalization) {
            viewed.addFirst(personal);
        }
        List<Post> result = postRepository.findByIdNotInOrderByCreateDatetimeDesc(viewed, pageable);
        if (result.isEmpty()) {
            redisService.cleaerViewed(userId);
            viewed = getListViewed(userId);
            result = postRepository.findByIdNotInOrderByCreateDatetimeDesc(viewed, pageable);
        }

        logger.info("Lấy bài viết thành công");
        return result.stream()
                .map(post -> {
                    try {
                        this.addViewed(userId, post.getId());
                        return this.mapToPostByUserIdResponse(post, userId);
                    } catch (AppCheckedException e) {
                        logger.error("Lỗi khi chuyển đổi dữ liệu post sang postResponse {}", e.getMessage());
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }


    @Override
    public List<PostResponse> getPostByFollowing(String userId) {
        Pageable pageable = PageRequest.of(0, 10);
        Map<String, List<String>> following = profileClient.listFollowing().getData();
        List<String> viewed = this.getViewedPostByFollowing(userId);

        List<Post> result = postRepository.findByUserIdAndIdNotInOrderByCreateDatetimeDesc(
                following.get("listFollowing"), viewed,pageable);
        return result.stream()
                .map(post -> {
                    try {
                        this.addViewedByFollowing(userId, post.getId());
                        return this.mapToPostByUserIdResponse(post, userId);
                    } catch (AppCheckedException e) {
                        logger.error("Lỗi khi chuyển đổi dữ liệu post sang postResponse trong getPostByFollowing {}", e.getMessage());
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private PostResponse mapToPostResponse(Post post) throws AppCheckedException {
        ProfileResponse profile = getProfile(post.getUserId());
        return PostResponse.builder()
                .id(post.getId())
                .content(post.getContent())
                .countLikes(post.getLikes().size())
                .countComments(getCountComment(post.getId()))
                .userId(post.getUserId())
                .lastName(profile.getLastName())
                .firstName(profile.getFirstName())
                .avatar(profile.getAvatar())
                .createDatetime(post.getCreateDatetime())
                .isLike(false)
                .build();
    }

    private PostResponse mapToPostByUserIdResponse(Post post, String userId) throws AppCheckedException {
        ProfileResponse profile = getProfile(post.getUserId());
//        boolean likePost = postRepository.existsByIdAndLikes(post.getId(), userId);
        //thêm vào danh sách những video đã xem
        if (profile == null) return null;
        return PostResponse.builder()
                .id(post.getId())
                .originPostId(post.getOriginPostId())
                .content(post.getContent())
                .countLikes(post.getLikes().size())
                .countComments(getCountComment(post.getId()))
                .userId(post.getUserId())
                .lastName(profile.getLastName())
                .firstName(profile.getFirstName())
                .avatar(profile.getAvatar())
                .createDatetime(post.getCreateDatetime())
                .isLike(post.getLikes().contains(userId))
                .isShare(post.getIsShare())
                .status(post.getStatus())
                .build();
    }

    @Override
    public ProfileResponse getProfile(String userId) throws AppCheckedException {
        try {
            return profileClient.getProfileResponseByUserId(userId);
        } catch (Exception e) {
            logger.error("Lỗi khi lấy thông tin profile người dùng {}", userId);
            return null;
        }
    }

    @Override
    public List<PostResponse> findByText(String text, String userId) {
        return postRepository.findByContentTextContainingIgnoreCase(text).stream()
                .map(post -> {
                    ProfileResponse profile;
                    try {
                        profile = getProfile(post.getUserId());
                    } catch (AppCheckedException e) {
                        logger.error("Lỗi khi lấy thông tin người dùng {}", post.getUserId());
                        throw new RuntimeException(e);
                    }
                    Integer countComment = commentRepository.countCommentsByPostId(post.getId());
//                    boolean likePost = postRepository.existsByIdAndLikes(post.getId(), post.getUserId());
                    return PostResponse.builder()
                            .id(post.getId())
                            .content(post.getContent())
                            .countLikes(post.getLikes().size())
                            .countComments(countComment)
                            .userId(post.getUserId())
                            .lastName(profile.getLastName())
                            .firstName(profile.getFirstName())
                            .avatar(profile.getAvatar())
                            .createDatetime(post.getCreateDatetime())
                            .isLike(post.getLikes().contains(userId))
                            .isShare(post.getIsShare())
                            .build();
                })
                .collect(Collectors.toList());
    }

    //thống k
    @Override
    public List<PostStatisticsDTO> countStatisticsPostToday(LocalDateTime startDate, LocalDateTime endDate) {

        List<PostStatisticsDTO> complaintStatisticsDTOS = postRepository.countByCreatedAtByHours(startDate, endDate);
        List<PostStatisticsDTO> result = new ArrayList<>();
        Map<String, Integer> mapComplaint = new HashMap<>();

        for (PostStatisticsDTO complaintStatisticsDTO : complaintStatisticsDTOS) {
            String hour = complaintStatisticsDTO.getHour();
            Integer count = complaintStatisticsDTO.getCount();
            mapComplaint.put(hour, count);
        }

        for (int hour = 0; hour < 24; hour++) {
            if (mapComplaint.containsKey(String.valueOf(hour))) {
                result.add(new PostStatisticsDTO(String.valueOf(hour), mapComplaint.get(String.valueOf(hour))));
            }
            result.add(new PostStatisticsDTO(String.valueOf(hour), 0));
        }
        ;

        return result;
    }

    @Override
    public List<PostStatisticsLongDateDTO> countStatisticsPostLongDay(LocalDateTime startDate, LocalDateTime endDate) {
        List<PostStatisticsLongDateDTO> postStatisticsDTOS = postRepository.countByDate(startDate, endDate);
        List<PostStatisticsLongDateDTO> result = new ArrayList<>();
//        Map<String, Integer> mapComplaint = new HashMap<>();
        LocalDateTime start = startDate.truncatedTo(ChronoUnit.DAYS);
        LocalDateTime end = endDate.plusDays(1).truncatedTo(ChronoUnit.DAYS);
        while (!start.equals(end)) {
            for (PostStatisticsLongDateDTO complaint : postStatisticsDTOS) {
//                LocalDateTime currentDay = start;
                if (complaint.getDate().truncatedTo(ChronoUnit.DAYS).equals(start)) {
                    result.add(complaint);
                } else {
                    result.add(new PostStatisticsLongDateDTO(start, 0));
                }

            }

            if (postStatisticsDTOS.isEmpty()) {
                result.add(new PostStatisticsLongDateDTO(start, 0));
            }
            start = start.plusDays(1);
        }
        return result;
    }

    @Override
    public PostResponse getPostById(String postId, String userId) throws AppCheckedException {
        Post postResponse = postRepository.findById(postId).orElseThrow(() -> new AppCheckedException("Không tìm thấy thông tin bài viết", StatusCode.POST_NOT_FOUND));
        return mapToPostByUserIdResponse(postResponse, userId);
    }

    private int getCountLikes(String postId) {
        return postRepository.countLikeByPost(postId);
    }

    private int getCountComment(String postId) {
        return commentRepository.countCommentsByPostId(postId);
    }

    private void addViewed(String userId, String postId) {
        redisService.viewed(userId, postId);
    }

    private List<String> getListViewed(String userId) {
        return redisService.getViewed(userId);
    }

    private List<String> getPersonalization(String userId) {
        return redisService.getPersonalization(userId);
    }

    private List<String> getViewedPostByFollowing(String userId) {
        return redisService.getViewedFollowing(userId);
    }

    private void addViewedByFollowing(String userId, String postId){
         redisService.viewedFollowing(userId, postId);
    }

}
