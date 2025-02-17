package com.fsocial.postservice.services;

import com.fsocial.postservice.dto.PostDTO;
import com.fsocial.postservice.dto.PostDTORequest;
import com.fsocial.postservice.entity.Post;
import com.fsocial.postservice.exception.AppCheckedException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

public interface PostService {
    PostDTO createPost(PostDTORequest post, String userId) throws AppCheckedException;

    PostDTO updatePost(PostDTORequest post, String postId) throws AppCheckedException;

    void deletePost(String postId) ;
}
