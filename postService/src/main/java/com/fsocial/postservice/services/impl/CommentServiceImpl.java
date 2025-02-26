package com.fsocial.postservice.services.impl;

import com.fsocial.postservice.Repository.CommentRepository;
import com.fsocial.postservice.dto.comment.CommentDTORequest;
import com.fsocial.postservice.entity.Comment;
import com.fsocial.postservice.entity.Content;
import com.fsocial.postservice.exception.AppCheckedException;
import com.fsocial.postservice.services.CommentService;
import com.fsocial.postservice.services.UploadMedia;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentServiceImpl implements CommentService {
    CommentRepository commentRepository;

    UploadMedia uploadMedia;

    @Override
    public Comment addComment(CommentDTORequest request) throws AppCheckedException, IOException {

        String[] uripostImage = new String[0];
        if(request.getMedia() != null && request.getMedia().length > 0) {
            MultipartFile[] validMedia = Arrays.stream(request.getMedia())
                    .filter(file -> file != null &&
                            !file.isEmpty() &&
                            file.getOriginalFilename() != null &&
                            !file.getOriginalFilename().isEmpty())
                    .toArray(MultipartFile[]::new);

            if (validMedia.length > 0) {
                uripostImage = uploadMedia.uploadMedia(validMedia);
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
        return commentRepository.save(commentRequest);
    }

    @Override
    public List<Comment> getComments(String postId) {
        return commentRepository.findByPostId(postId);
    }
}
