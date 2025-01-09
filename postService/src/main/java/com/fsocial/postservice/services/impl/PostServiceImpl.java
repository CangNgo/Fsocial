package com.fsocial.postservice.services.impl;

import com.fsocial.postservice.Repository.PostRepository;
import com.fsocial.postservice.dto.PostDTO;
import com.fsocial.postservice.entity.Post;
import com.fsocial.postservice.exception.AppCheckedException;
import com.fsocial.postservice.exception.StatusCode;
import com.fsocial.postservice.mapper.PostMapper;
import com.fsocial.postservice.services.PostService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    @Autowired
    PostRepository postRepository;
    @Autowired
    PostMapper postMapper;
    @Override
    public PostDTO createPost(PostDTO post) throws AppCheckedException {
        try {
            Post result = postMapper.toPost(post) ;
            result = postRepository.save(result);
            return postMapper.toPostDTO(result);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}
