package com.fsocial.postservice.mapper;

import com.fsocial.postservice.dto.post.PostDTO;
import com.fsocial.postservice.dto.post.PostDTORequest;
import com.fsocial.postservice.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostMapper {
    Post toPost(PostDTO postDTO);

    PostDTO toPostDTO(Post post);

    @Mapping(target = "content.text", source = "text")
    @Mapping(target = "content.media", ignore = true)
    @Mapping(target = "countLikes", source = "countLikes")
    @Mapping(target = "createdAt", source = "createdAt")
    Post toPost(PostDTORequest dto);
}
