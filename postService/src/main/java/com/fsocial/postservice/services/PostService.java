package com.fsocial.postservice.services;

import com.fsocial.postservice.dto.PostDTO;
import com.fsocial.postservice.entity.Post;
import com.fsocial.postservice.exception.AppCheckedException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public interface PostService {
    PostDTO createPost(PostDTO post) throws AppCheckedException;
}
