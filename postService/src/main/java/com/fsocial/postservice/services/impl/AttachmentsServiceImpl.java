package com.fsocial.postservice.services.impl;

import com.fsocial.postservice.dto.Attachments.AttachmentDTO;
import com.fsocial.postservice.entity.Attachments;
import com.fsocial.postservice.mapper.AttachmentMapper;
import com.fsocial.postservice.repository.AttachmentsRepository;
import com.fsocial.postservice.services.AttachmentsService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AttachmentsServiceImpl implements AttachmentsService {

    AttachmentsRepository attachmentsRepository;
    AttachmentMapper attachmentMapper;

    @Override
    public AttachmentDTO save(AttachmentDTO dto) {
        return attachmentMapper.toDTO(attachmentsRepository.save(Attachments.builder()
                .url(dto.getUrl()).resourceType(dto.getResourceType()).fileType(dto.getFileType()).size(dto.getSize()).ownerId(dto.getOwnerId()).publicId(dto.getPublicId()).build()));
    }
}
