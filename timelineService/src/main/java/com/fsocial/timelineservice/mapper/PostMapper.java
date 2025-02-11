package com.fsocial.timelineservice.mapper;

import com.fsocial.timelineservice.dto.post.PostDTO;
import com.fsocial.timelineservice.dto.post.PostDTORequest;
import com.fsocial.timelineservice.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Optional;

@Mapper(componentModel = "spring")
public interface PostMapper {
    Post toPost(PostDTO postDTO);

    PostDTO toPostDTO(Optional<Post> post);

    @Mapping(target = "content.text", source = "text")
    @Mapping(target = "content.media", ignore = true)
    @Mapping(target = "countLikes", source = "countLikes")
    Post toPost(PostDTORequest dto);
}
