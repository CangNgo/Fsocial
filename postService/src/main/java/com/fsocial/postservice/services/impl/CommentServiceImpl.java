package com.fsocial.postservice.services.impl;

import com.fsocial.postservice.entity.Post;
import com.fsocial.postservice.enums.ErrorCode;
import com.fsocial.postservice.enums.MessageNotice;
import com.fsocial.postservice.exception.AppCheckedException;
import com.fsocial.postservice.exception.AppException;
import com.fsocial.postservice.repository.CommentRepository;
import com.fsocial.postservice.dto.comment.CommentDTORequest;
import com.fsocial.postservice.entity.Comment;
import com.fsocial.postservice.entity.Content;
import com.fsocial.postservice.repository.PostRepository;
import com.fsocial.postservice.services.CommentService;
import com.fsocial.postservice.services.UploadMedia;
import com.fsocial.postservice.services.KafkaService;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CommentServiceImpl implements CommentService {
    CommentRepository commentRepository;

    UploadMedia uploadMedia;
    KafkaService kafkaService;
    PostRepository postRepository;

    @Override
    @Transactional
    public Comment addComment(CommentDTORequest request) {
        String[] mediaUrls = extractValidMedia(request.getMedia());

        String postId = request.getPostId();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

        Comment commentRequest = buildComment(request, mediaUrls);
        commentRequest.setCreatedAt(LocalDateTime.now());
        Comment savedComment = commentRepository.save(commentRequest);

        // Send request to notification
        String ownerId = post.getUserId();
        String userId = request.getUserId();
        kafkaService.sendNotification(ownerId, userId, MessageNotice.NOTIFICATION_COMMENT);

        return savedComment;
    }

    private String[] extractValidMedia(MultipartFile[] media) {
        if (media == null || media.length == 0) return new String[0];

        try {
            MultipartFile[] validMedia = Arrays.stream(media)
                    .filter(file -> file != null && !file.isEmpty()
                            && file.getOriginalFilename() != null
                            && !file.getOriginalFilename().isEmpty())
                    .toArray(MultipartFile[]::new);

            return validMedia.length > 0 ? uploadMedia.uploadMedia(validMedia) : new String[0];
        } catch (Exception e) {
            log.error("Lỗi khi tải lên tệp: {}", e.getMessage(), e);
            throw new AppException(ErrorCode.UPLOAD_MEDIA_FAILED);
        }
    }

    private Comment buildComment(CommentDTORequest request, String[] mediaUrls) {
        return Comment.builder()
                .countReplyComment(0)
                .countLikes(0)
                .reply(false)
                .postId(request.getPostId())
                .userId(request.getUserId())
                .content(Content.builder()
                        .text(request.getText())
                        .media(mediaUrls)
                        .HTMLText(request.getHTMLText())
                        .build())
                .build();
    }

    @Override
    public List<Comment> getComments(String postId) {
        return commentRepository.findByPostId(postId);
    }
}
