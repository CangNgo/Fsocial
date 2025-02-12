package com.fsocial.postservice.services.impl;

import com.fsocial.postservice.Repository.PostRepository;
import com.fsocial.postservice.Repository.httpClient.ProfileClient;
import com.fsocial.postservice.dto.ContentDTO;
import com.fsocial.postservice.dto.PostDTO;
import com.fsocial.postservice.dto.PostDTORequest;
import com.fsocial.postservice.dto.profile.ProfileDTO;
import com.fsocial.postservice.entity.Post;
import com.fsocial.postservice.exception.AppCheckedException;
import com.fsocial.postservice.exception.StatusCode;
import com.fsocial.postservice.mapper.ContentMapper;
import com.fsocial.postservice.mapper.PostMapper;
import com.fsocial.postservice.services.PostService;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    public PostDTO createPost(PostDTORequest postRequest, String userId) throws AppCheckedException {
        try {
            //upload ảnh
            String[] uripostImage = uploadImage.uploadImage(postRequest.getMedia());
            Post post = postMapper.toPost(postRequest);
            //thêm userId
            post.setUserId(userId);
            //thêm content
            ContentDTO content = ContentDTO.builder()
                    .text(postRequest.getText())
                    .HTMLText(postRequest.getHTMLText())
                    .media(uripostImage)
            .build();

            post.setCountComments(0);
            post.setCountLikes(0);
            post.setContent(contentMapper.toContent(content));
            post.setCreatedBy(userId);
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
    public List<Post> getPosts() {
        return postRepository.findAll();
    }

}
