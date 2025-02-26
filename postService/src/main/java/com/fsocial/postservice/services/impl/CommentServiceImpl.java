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
import com.fsocial.postservice.services.KafkaService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CommentServiceImpl implements CommentService {
    CommentRepository commentRepository;
    UploadImageImpl uploadImage;
    KafkaService kafkaService;
    PostRepository postRepository;

    @Override
    @Transactional
    public Comment addComment(CommentDTORequest request)  throws AppCheckedException, IOException{
        String[] uripostImage = new String[0];
        if(request.getMedia() != null && request.getMedia().length > 0) {
            MultipartFile[] validMedia = Arrays.stream(request.getMedia())
                    .filter(file -> file != null &&
                            !file.isEmpty() &&
                            file.getOriginalFilename() != null &&
                            !file.getOriginalFilename().isEmpty())
                    .toArray(MultipartFile[]::new);

            if (validMedia.length > 0) {
                uripostImage = uploadImage.uploadImage(validMedia);
            }
        };

        Comment commentRequest = Comment.builder()
                .countReplyComment(0)
                .countLikes(0)
                .reply(false)
                .postId(request.getPostId())
                .userId(request.getUserId())
                .content(
                        Content.builder()
                                .text(request.getText())
                                .media(uripostImage)
                                .HTMLText(request.getHTMLText())
                                .build()
                )
                .build();

        commentRequest.setCreatedAt(LocalDateTime.now());
        Comment savedComment = commentRepository.save(commentRequest);

        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

        // Send request to notification
        String ownerId = post.getUserId();
        String userId = request.getUserId();
        kafkaService.sendNotification(ownerId, userId, MessageNotice.NOTIFICATION_COMMENT);

        return savedComment;
    }

    @Override
    public List<Comment> getComments(String postId) {
        return commentRepository.findByPostId(postId);
    }
}
