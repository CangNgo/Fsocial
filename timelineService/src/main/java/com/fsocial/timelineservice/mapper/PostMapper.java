package com.fsocial.timelineservice.mapper;

import com.fsocial.timelineservice.dto.post.PostDTO;
import com.fsocial.timelineservice.dto.profile.ProfileResponse;
import com.fsocial.timelineservice.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostMapper {
    Post toPost(PostDTO postDTO);

}
