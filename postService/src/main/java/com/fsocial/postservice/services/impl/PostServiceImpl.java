package com.fsocial.postservice.services.impl;

import com.fsocial.postservice.Repository.PostRepository;
import com.fsocial.postservice.Repository.httpClient.ProfileClient;
import com.fsocial.postservice.dto.ContentDTO;
import com.fsocial.postservice.dto.post.PostDTO;
import com.fsocial.postservice.dto.post.PostDTORequest;
import com.fsocial.postservice.entity.Content;
import com.fsocial.postservice.entity.Post;
import com.fsocial.postservice.exception.AppCheckedException;
import com.fsocial.postservice.exception.StatusCode;
import com.fsocial.postservice.mapper.ContentMapper;
import com.fsocial.postservice.mapper.PostMapper;
import com.fsocial.postservice.services.PostService;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostServiceImpl implements PostService {
    PostRepository postRepository;
    UploadImageImpl uploadImage;
    PostMapper postMapper;
    ContentMapper contentMapper;
    ProfileClient profileClient;

    @Override
    public PostDTO createPost(PostDTORequest postRequest) throws AppCheckedException {
        try {
            //upload ảnh
            String[] uripostImage = new String[0];
            if(postRequest.getMedia() != null && postRequest.getMedia().length > 0) {
                MultipartFile[] validMedia = Arrays.stream(postRequest.getMedia())
                        .filter(file -> file != null &&
                                !file.isEmpty() &&
                                file.getOriginalFilename() != null &&
                                !file.getOriginalFilename().isEmpty())
                        .toArray(MultipartFile[]::new);

                if (validMedia.length > 0) {
                    uripostImage = uploadImage.uploadImage(validMedia);
                }
            };
            Post post = postMapper.toPost(postRequest);
            //thêm userId
            post.setUserId(postRequest.getUserId());
            //thêm content
            ContentDTO content = ContentDTO.builder()
                    .text(postRequest.getText())
                    .HTMLText(postRequest.getHTMLText())
                    .media(uripostImage)
                    .build();

            post.setCountComments(0);
            post.setCountLikes(0);
            post.setContent(contentMapper.toContent(content));
            post.setCreatedBy(postRequest.getHTMLText());
            post.setCreatedAt(LocalDateTime.now());
            //kết quả trả về
            return postMapper.toPostDTO(postRepository.save(post));
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            throw new AppCheckedException("Không thể thêm bài post vào database", StatusCode.CREATE_POST_FAILED);
        } catch (IOException e) {
            throw new AppCheckedException("Không thể upload ảnh lên cloud", StatusCode.UPLOAD_FILE_FAILED);
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
        existingPost.setUpdatedAt(LocalDateTime.now());
        return postMapper.toPostDTO(postRepository.save(existingPost));
    }

    @Override
    public void deletePost(String postId) {
        postRepository.deleteById(postId);
    }
}
