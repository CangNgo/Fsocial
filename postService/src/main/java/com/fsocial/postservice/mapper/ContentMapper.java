package com.fsocial.postservice.mapper;

import com.fsocial.postservice.dto.ContentDTO;
import com.fsocial.postservice.entity.Content;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ContentMapper {
    Content toContent(ContentDTO contentDTO);
}
