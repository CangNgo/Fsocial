package com.fsocial.postservice.mapper;

import com.fsocial.postservice.dto.Attachments.AttachmentDTO;
import com.fsocial.postservice.entity.Attachments;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AttachmentMapper {
    AttachmentDTO toDTO(Attachments attachments);
    Attachments toEntity(AttachmentDTO dto);
    List<AttachmentDTO> toDTOs(List<Attachments> attachments);
}
