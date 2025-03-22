package com.fsocial.postservice.services.impl;

import com.fsocial.event.NotificationRequest;
import com.fsocial.postservice.dto.ContentDTO;
import com.fsocial.postservice.dto.post.PostDTO;
import com.fsocial.postservice.dto.post.PostDTORequest;
import com.fsocial.postservice.dto.post.PostShareDTORequest;
import com.fsocial.postservice.entity.Content;
import com.fsocial.postservice.entity.Post;
import com.fsocial.postservice.enums.TopicKafka;
import com.fsocial.postservice.exception.AppCheckedException;
import com.fsocial.postservice.exception.AppUnCheckedException;
import com.fsocial.postservice.exception.StatusCode;
import com.fsocial.postservice.mapper.ContentMapper;
import com.fsocial.postservice.mapper.PostMapper;
import com.fsocial.postservice.repository.PostRepository;
import com.fsocial.postservice.services.KafkaService;
import com.fsocial.postservice.services.PostService;
import com.fsocial.postservice.services.UploadMedia;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
    RestTemplate restTemplate;
    KafkaService kafkaService;
    String profileServiceUrl = "http://localhost:8888/profile";
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
                kafkaService.sendNotification(NotificationRequest.builder()
                        .ownerId(post.getUserId())
                        .receiverId(userId)
                        .topic(TopicKafka.TOPIC_LIKE.getTopic())
                        .postId(postId)
                        .build());

                log.info("Đã gửi thông báo LIKE đến Antony");

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
                .userId(postRequest.getUserId())
                .isShare(true)
                .originPostId(postRequest.getOriginPostId())
                .likes(new ArrayList<>())
                .createDatetime(LocalDateTime.now())
                .build();

        return postMapper.toPostDTO(postRepository.save(post));
    }

    private ContentDTO buildContent(String html, String text, String[] media){
        return ContentDTO.builder()
                .text(text)
                .HTMLText(html)
                .media(media)
                .build();
    }

    private ContentDTO buildContent(String html, String text){
        return ContentDTO.builder()
                .text(text)
                .HTMLText(html)
                .build();
    }

    private Post buildPost(ContentDTO contentDTO, PostDTORequest postRequest){
        Post post =postMapper.toPost(postRequest);

        //thêm userId
        post.setUserId(postRequest.getUserId());
        post.setContent(contentMapper.toContent(contentDTO));
        post.setCreateDatetime(LocalDateTime.now());
        post.setLikes(new ArrayList<>());
        return  post;
    }

    @Override
    public List<Post> getPostsByUser(String userId, String requesterId) {
        // Gọi API để kiểm tra trạng thái chế độ riêng tư
        Boolean isPrivacyEnabled = restTemplate.getForObject(profileServiceUrl + "/update-visibility/" + userId, Boolean.class);

        // Nếu chế độ riêng tư tắt, trả về tất cả bài post của người dùng
        if (!isPrivacyEnabled) {
            return postRepository.findByUserId(userId);
        }

        // Nếu chế độ riêng tư bật, kiểm tra xem người yêu cầu có phải là người theo dõi hay không
        boolean isFollowing = restTemplate.getForObject(profileServiceUrl + "/is-following/" + requesterId, Boolean.class);

        if (isFollowing) {
            return postRepository.findByUserId(userId);
        }

        // Nếu không phải người theo dõi và chế độ riêng tư bật, không cho phép xem bài viết
        return Collections.emptyList();
    }

}
