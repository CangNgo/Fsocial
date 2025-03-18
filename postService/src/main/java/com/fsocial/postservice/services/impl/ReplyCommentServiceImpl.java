package com.fsocial.postservice.services.impl;

import com.cloudinary.provisioning.Account;
import com.fsocial.postservice.dto.replyComment.ReplyCommentUpdateDTORequest;
import com.fsocial.postservice.entity.Comment;
import com.fsocial.postservice.exception.AppCheckedException;
import com.fsocial.postservice.exception.StatusCode;
import com.fsocial.postservice.repository.ReplyCommentRepository;
import com.fsocial.postservice.dto.replyComment.ReplyCommentRequest;
import com.fsocial.postservice.entity.Content;
import com.fsocial.postservice.entity.ReplyComment;
import com.fsocial.postservice.mapper.ReplyCommentMapper;
import com.fsocial.postservice.repository.httpClient.Accountclient;
import com.fsocial.postservice.services.ReplyCommentService;
import com.fsocial.postservice.services.UploadMedia;
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

    UploadMedia uploadMedia;

    Accountclient accountclient;

    @Override
    public ReplyComment addReplyComment(ReplyCommentRequest request) throws AppCheckedException {
        String[] uripostImage = new String[0];
        if (request.getMedia() != null && request.getMedia().length > 0) {
            MultipartFile[] validMedia = Arrays.stream(request.getMedia())
                    .filter(file -> file != null &&
                            !file.isEmpty() &&
                            file.getOriginalFilename() != null &&
                            !file.getOriginalFilename().isEmpty())
                    .toArray(MultipartFile[]::new);

            if (validMedia.length > 0) {
                uripostImage = uploadMedia.uploadMedia(validMedia);
            }
        }
        ;

        ReplyComment replyComment = replyCommentMapper.toEntity(request);
        replyComment.setContent(Content.builder()
                .media(uripostImage)
                .text(request.getText())
                .HTMLText(request.getHTMLText())
                .build());

        return replyCommentRepository.save(replyComment);
    }
@Override
    public String deleteReplyComment(String idReplyComment) {
        replyCommentRepository.deleteById(idReplyComment);
        return "Xóa replycomment thành công";
    }

    @Override
    public ReplyComment updateReplyComment(ReplyCommentUpdateDTORequest upateReply) throws AppCheckedException {
        ReplyComment instance = replyCommentRepository.findById(upateReply.getReplyCommentId()).orElseThrow(
                () -> new AppCheckedException("Reply comment không tồn tại", StatusCode.REPLY_COMMENT_NOT_FOUND));
       if(userExists(upateReply.getUserId())){
           throw new AppCheckedException("User không tồn tại", StatusCode.USER_NOT_FOUND);
       }
       instance.setContent(Content.builder()
                       .text(upateReply.getText())
                       .HTMLText(upateReply.getHTMLText())
               .build());
       return replyCommentRepository.save(instance);
    }

    private boolean userExists(String userId) {
        return accountclient.existsAccountByUserId(userId).getData().containsKey("exists");
    }

}
