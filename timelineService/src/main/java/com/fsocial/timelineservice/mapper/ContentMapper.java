package com.fsocial.timelineservice.mapper;

import com.fsocial.timelineservice.dto.post.ContentDTO;
import com.fsocial.timelineservice.entity.Content;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ContentMapper {
    Content toContent(ContentDTO contentDTO);
}
