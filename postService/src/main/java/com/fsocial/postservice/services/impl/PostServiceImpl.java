package com.fsocial.postservice.services.impl;

import com.fsocial.postservice.dto.ContentDTO;
import com.fsocial.postservice.dto.post.*;
import com.fsocial.postservice.dto.profile.ProfileResponse;
import com.fsocial.postservice.entity.Content;
import com.fsocial.postservice.entity.Owner;
import com.fsocial.postservice.entity.Post;
import com.fsocial.postservice.exception.AppCheckedException;
import com.fsocial.postservice.exception.StatusCode;
import com.fsocial.postservice.mapper.ContentMapper;
import com.fsocial.postservice.mapper.PostMapper;
import com.fsocial.postservice.publisher.PostEventPublisher;
import com.fsocial.postservice.repository.CommentRepository;
import com.fsocial.postservice.repository.PostRepository;
import com.fsocial.postservice.repository.httpClient.NotificationClient;
import com.fsocial.postservice.repository.httpClient.ProfileClient;
import com.fsocial.postservice.services.PostService;
import com.fsocial.postservice.services.RedisService;
import com.fsocial.postservice.services.UploadMedia;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PostServiceImpl implements PostService {
    PostRepository postRepository;
    MongoTemplate mongoTemplate;
    UploadMedia uploadMedia;
    PostMapper postMapper;
    ContentMapper contentMapper;
    RedisService redisService;
    RestTemplate restTemplate;
    NotificationClient notificationClient;
    ProfileClient profileClient;
    PostEventPublisher postEventPublisher;

    static Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);
    //    KafkaService kafkaService;
    String profileServiceUrl = "http://localhost:8888/profile";
    private final RedisServiceImpl redisServiceImpl;
    CommentRepository commentRepository;
    ProfileClient postServiceProfileClient;

    @Override
    @Transactional
    public PostDTO createPost(PostDTORequest postRequest) throws AppCheckedException {
        try {
            //upload ảnh
            String[] uripostImage = new String[0];
            if (postRequest.getMedia() != null && postRequest.getMedia().length > 0) {
                MultipartFile[] validMedia = Arrays.stream(postRequest.getMedia())
                    .filter(file -> file != null &&
                        !file.isEmpty() &&
                        file.getOriginalFilename() != null &&
                        !file.getOriginalFilename().isEmpty())
                    .toArray(MultipartFile[]::new);

                if (validMedia.length > 0) {
                    uripostImage = uploadMedia.uploadMedia(validMedia);
                }
            }
            ContentDTO contentDTO = buildContent(postRequest.getHTMLText(),
                postRequest.getText(),
                uripostImage);
            Post post = buildPost(contentDTO, postRequest);
//            this.notificationClient.createNotification(NoticeRequest.builder()
//                            .title("")
//                    .build());
            //kết quả trả về
            return postMapper.toPostDTO(postRepository.save(post));
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            throw new AppCheckedException("Không thể thêm bài post vào database", StatusCode.CREATE_POST_FAILED);
        }
    }

    @Override
    public PostDTO updatePost(PostDTORequest post, String postId) throws AppCheckedException {
        Post existingPost = postRepository.findById(postId)
            .orElseThrow(() -> new AppCheckedException("Post not found", StatusCode.POST_NOT_FOUND));
        //Nếu tìm thấy thì cập nhật thông tin

        existingPost.setContent(Content.builder()
            .text(post.getText())
            .HTMLText(post.getHTMLText())
            .media(existingPost.getContent().getMedia())
            .build());
        //cap nhat thoi gian
        existingPost.setUpdatedAt(LocalDateTime.now());
        return postMapper.toPostDTO(postRepository.save(existingPost));
    }

    @Override
    public void deletePost(String postId) {

        postEventPublisher.eventDeletePost(postId);

        postRepository.deleteById(postId);
    }

    @Override
    public boolean toggleLike(String postId, String userId) throws Exception {

        boolean existed = postRepository.existsByIdAndLikes(postId, userId);

        try {
            if (!existed) {
                this.addLike(postId, userId);

                // Gửi thông báo đến người dùng
                Post post = postRepository.findById(postId).orElseThrow();
                return true;
            } else {
                this.removeLike(postId, userId);
                return false;
            }
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public void addLike(String postId, String userId) {
        Query query = new Query(Criteria.where("_id").is(postId));
        Update update = new Update().addToSet("likes", userId);
        mongoTemplate.updateFirst(query, update, Post.class);
    }

    public void removeLike(String postId, String userId) {
        Query query = new Query(Criteria.where("_id").is(postId));
        Update update = new Update().pull("likes", userId);
        mongoTemplate.updateFirst(query, update, Post.class);
    }

    @Override
    public Integer CountLike(String postId, String userId) {
        Integer countLike = postRepository.countLikeByPost(postId);
        return countLike == null ? 0 : countLike;
    }

    @Override
    public PostDTO sharePost(PostShareDTORequest postRequest) {
        ContentDTO contentDTO = buildContent(postRequest.getHTMLText(),
            postRequest.getText());
        Post post = Post.builder()
            .content(contentMapper.toContent(contentDTO))
            .owner(Owner.builder()
                .userId(postRequest.getUserId())
                .build())
            .isShare(true)
            .originPostId(postRequest.getOriginPostId())
            .likes(new ArrayList<>())
            .createDatetime(LocalDateTime.now())
            .build();
        //thêm vào persional
        redisService.personalization(postRequest.getUserId(), post.getOwner().getUserId());
        return postMapper.toPostDTO(postRepository.save(post));
    }

    private ContentDTO buildContent(String html, String text, String[] media) {
        return ContentDTO.builder()
            .text(text)
            .HTMLText(html)
            .media(media)
            .build();
    }

    private ContentDTO buildContent(String html, String text) {
        return ContentDTO.builder()
            .text(text)
            .HTMLText(html)
            .build();
    }

    private Post buildPost(ContentDTO contentDTO, PostDTORequest postRequest) {
        Post post = postMapper.toPost(postRequest);

        //thêm userId
        post.setOwner(Owner.builder()
            .userId(postRequest.getUserId())
            .build());
        post.setContent(contentMapper.toContent(contentDTO));
        post.setCreateDatetime(LocalDateTime.now());
        post.setLikes(new ArrayList<>());
        return post;
    }

    @Override
    public List<Post> getPostsByUser(String userId, String requesterId) {
        // Gọi API để kiểm tra trạng thái chế độ riêng tư
        Boolean isPrivacyEnabled = restTemplate.getForObject(profileServiceUrl + "/update-visibility/" + userId, Boolean.class);

        // Nếu chế độ riêng tư tắt, trả về tất cả bài post của người dùng
        if (!isPrivacyEnabled) {
            return postRepository.findByOwnerUserId(userId);
        }

        // Nếu chế độ riêng tư bật, kiểm tra xem người yêu cầu có phải là người theo dõi hay không
        boolean isFollowing = restTemplate.getForObject(profileServiceUrl + "/is-following/" + requesterId, Boolean.class);

        if (isFollowing) {
            return postRepository.findByOwnerUserId(userId);
        }

        // Nếu không phải người theo dõi và chế độ riêng tư bật, không cho phép xem bài viết
        return Collections.emptyList();
    }

    // Methods from timelineService
    @Override
    public List<PostResponse> getPostsByUserId(String userId) {
        Pageable pageable = PageRequest.of(0, 10);
        List<String> viewed = redisService.getViewed(userId);
        List<String> personalization = redisService.getPersonalization(userId);
        for (String personal : personalization) {
            viewed.addFirst(personal);
        }
        List<Post> result = postRepository.findByIdNotInOrderByCreateDatetimeDesc(viewed, pageable);
        if (result.isEmpty()) {
            redisService.cleaerViewed(userId);
            viewed = redisService.getViewed(userId);
            result = postRepository.findByIdNotInOrderByCreateDatetimeDesc(viewed, pageable);
        }

        log.info("Lấy bài viết thành công");
        return result.stream()
            .map(post -> {
                try {
                    redisService.viewed(userId, post.getId());
                    return mapToPostByUserIdResponse(post, userId);
                } catch (com.fsocial.postservice.exception.AppCheckedException e) {
                    log.error("Lỗi khi chuyển đổi dữ liệu post sang postResponse {}", e.getMessage());
                    return null;
                }
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    @Override
    public PostResponse getPostById(String postId, String userId) throws com.fsocial.postservice.exception.AppCheckedException {
        Post postResponse = postRepository.findById(postId).orElseThrow(() -> new com.fsocial.postservice.exception.AppCheckedException("Không tìm thấy thông tin bài viết", StatusCode.POST_NOT_FOUND));
        return mapToPostByUserIdResponse(postResponse, userId);
    }

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

        return result;
    }

    @Override
    public List<PostStatisticsLongDateDTO> countStatisticsPostLongDay(LocalDateTime startDate, LocalDateTime endDate) {
        List<PostStatisticsLongDateDTO> postStatisticsDTOS = postRepository.countByDate(startDate, endDate);
        List<PostStatisticsLongDateDTO> result = new ArrayList<>();
        LocalDateTime start = startDate.truncatedTo(ChronoUnit.DAYS);
        LocalDateTime end = endDate.plusDays(1).truncatedTo(ChronoUnit.DAYS);
        while (!start.equals(end)) {
            for (PostStatisticsLongDateDTO complaint : postStatisticsDTOS) {
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
    public List<PostResponse> getPostByFollowing(String userId) {
        Pageable pageable = PageRequest.of(0, 10);
        Map<String, List<String>> following = profileClient.listFollowing().getData();
        List<String> viewed = this.getViewedPostByFollowing(userId);

        List<Post> result = postRepository.findByOwnerUserIdAndIdNotInOrderByCreateDatetimeDesc(
            following.get("listFollowing"), viewed, pageable);
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

    public List<Post> migratePostEntity() {
        List<Post> allPost = postRepository.findAll();
        log.info("Bat dau migrate: ");
        return allPost.stream()
            .map(post -> {
                try {
                    PostResponse res = mapToPostByUserIdResponse(post, post.getOwner().getUserId());
                    log.info("migratePostEntity: {}", res);
                    if (res == null) {
//                        postRepository.deleteById(post.getId());
                        log.info("Xóa bài post mà user không còn tồn tại: {}", post.getContent().getText());
                        return null;
                    }
                    String lastName = res.getLastName() == null ? "" : res.getLastName();
                    String firstName = res.getFirstName() == null ? "" : res.getFirstName();
                    return Post.builder()
                        .id(post.getId())
                        .likes(post.getLikes())
                        .originPostId(post.getOriginPostId())
                        .isShare(post.getIsShare())
                        .status(post.getStatus())
                        .createDatetime(post.getCreateDatetime())

                        //cap nhat thong tin moi vao entity
                        .owner(Owner.builder()
                            .displayName(lastName + " " + firstName)
                            .avatar(res.getAvatar())
                            .userId(post.getOwner().getUserId())
                            .build())

                        .createdAt(post.getCreatedAt())
                        .createdBy(post.getCreatedBy())
                        .updatedAt(post.getUpdatedAt())
                        .updatedBy(post.getUpdatedBy())
                        .content(post.getContent())

                        .build();

                } catch (AppCheckedException e) {
                    throw new RuntimeException(e);
                }
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    private PostResponse mapToPostResponse(Post post) throws AppCheckedException {
        ProfileResponse profile = getProfile(post.getOwner().getUserId());
        return PostResponse.builder()
            .id(post.getId())
            .content(post.getContent())
            .countLikes(post.getLikes().size())
            .countComments(getCountComment(post.getId()))
            .userId(post.getOwner().getUserId())
            .lastName(profile.getLastName())
            .firstName(profile.getFirstName())
            .avatar(profile.getAvatar())
            .createDatetime(post.getCreateDatetime())
            .isLike(false)
            .build();
    }

    private PostResponse mapToPostByUserIdResponse(Post post, String userId) throws AppCheckedException {
        ProfileResponse profile = getProfile(post.getOwner().getUserId());
//        boolean likePost = postRepository.existsByIdAndLikes(post.getId(), userId);
        //thêm vào danh sách những video đã xem
        if (profile == null) return null;
        return PostResponse.builder()
            .id(post.getId())
            .originPostId(post.getOriginPostId())
            .content(post.getContent())
            .countLikes(post.getLikes().size())
            .countComments(getCountComment(post.getId()))
            .userId(post.getOwner().getUserId())
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
                    profile = getProfile(post.getOwner().getUserId());
                } catch (AppCheckedException e) {
                    logger.error("Lỗi khi lấy thông tin người dùng {}", post.getOwner().getUserId());
                    throw new RuntimeException(e);
                }
                Integer countComment = commentRepository.countCommentsByPostId(post.getId());
//                    boolean likePost = postRepository.existsByIdAndLikes(post.getId(), post.getOwner().getUserId()());
                return PostResponse.builder()
                    .id(post.getId())
                    .content(post.getContent())
                    .countLikes(post.getLikes().size())
                    .countComments(countComment)
                    .userId(post.getOwner().getUserId())
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

    private void addViewedByFollowing(String userId, String postId) {
        redisService.viewedFollowing(userId, postId);
    }

}
