package com.fsocial.postservice.mapper;

import com.fsocial.postservice.dto.PostDTO;
import com.fsocial.postservice.entity.Post;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PostMapper {
    Post toPost(PostDTO postDTO);

    PostDTO toPostDTO(Post post);
}
