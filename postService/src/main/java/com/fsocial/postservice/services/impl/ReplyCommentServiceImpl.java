package com.fsocial.postservice.services.impl;

import com.fsocial.postservice.repository.ReplyCommentRepository;
import com.fsocial.postservice.dto.replyComment.ReplyCommentRequest;
import com.fsocial.postservice.entity.Content;
import com.fsocial.postservice.entity.ReplyComment;
import com.fsocial.postservice.exception.AppCheckedException;
import com.fsocial.postservice.mapper.ReplyCommentMapper;
import com.fsocial.postservice.services.ReplyCommentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor()
public class ReplyCommentServiceImpl implements ReplyCommentService {

    ReplyCommentRepository replyCommentRepository;

    ReplyCommentMapper replyCommentMapper;

    UploadImageImpl uploadImage;

    @Override
    public ReplyComment addReplyComment(ReplyCommentRequest request) throws AppCheckedException, IOException {
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

        ReplyComment replyComment = replyCommentMapper.toEntity(request);
        replyComment.setContent(Content.builder()
                .media(uripostImage)
                .text(request.getText())
                .HTMLText(request.getHTMLText())
                .build());

        return replyCommentRepository.save(replyComment);
    }
}
