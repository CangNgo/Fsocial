package com.fsocial.postservice.services;

import com.fsocial.postservice.dto.Attachments.AttachmentDTO;
import com.fsocial.postservice.exception.AppCheckedException;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AttachmentsService {

    AttachmentDTO save(AttachmentDTO dto);
    List<AttachmentDTO> getAttachmentsByOwnerId(String ownerId, Pageable pageable) throws AppCheckedException;
    List<AttachmentDTO> getAttachmentsByType(String ownerId,String fileType,  Pageable pageable) throws AppCheckedException;

}
