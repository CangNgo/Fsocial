package com.fsocial.postservice.mapper;

import com.fsocial.postservice.dto.ContentDTORequest;
import com.fsocial.postservice.dto.PostDTO;
import com.fsocial.postservice.dto.PostDTORequest;
import com.fsocial.postservice.entity.Content;
import com.fsocial.postservice.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface PostMapper {
    Post toPost(PostDTO postDTO);

    PostDTO toPostDTO(Post post);

    @Mapping(target = "content.text", source = "text")
    @Mapping(target = "content.media", ignore = true)
    @Mapping(target = "countLikes", source = "countLikes")
    Post toPost(PostDTORequest dto);
}
