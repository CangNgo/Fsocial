package com.fsocial.postservice.services;

import com.fsocial.postservice.dto.post.LikePostDTO;
import com.fsocial.postservice.dto.post.PostDTO;
import com.fsocial.postservice.dto.post.PostDTORequest;
import com.fsocial.postservice.exception.AppCheckedException;

public interface PostService {
    PostDTO createPost(PostDTORequest request) throws AppCheckedException;

    PostDTO updatePost(PostDTORequest post, String postId) throws AppCheckedException;

    void deletePost(String postId) ;

    boolean toggleLike(String postId, String userId) throws Exception;


    Integer CountLike(String postId, String userId);
}
