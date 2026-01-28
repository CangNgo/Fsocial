package com.fsocial.postservice.services.impl;

import com.fsocial.postservice.dto.Attachments.AttachmentDTO;
import com.fsocial.postservice.entity.Attachments;
import com.fsocial.postservice.exception.AppCheckedException;
import com.fsocial.postservice.exception.StatusCode;
import com.fsocial.postservice.mapper.AttachmentMapper;
import com.fsocial.postservice.repository.AttachmentsRepository;
import com.fsocial.postservice.repository.httpClient.Accountclient;
import com.fsocial.postservice.services.AttachmentsService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AttachmentsServiceImpl implements AttachmentsService {

    AttachmentsRepository attachmentsRepository;
    AttachmentMapper attachmentMapper;
    Accountclient accountclient;

    @Override
    public AttachmentDTO save(AttachmentDTO dto) {
        return attachmentMapper.toDTO(attachmentsRepository.save(Attachments.builder()
                .url(dto.getUrl()).resourceType(dto.getResourceType()).fileType(dto.getFileType()).size(dto.getSize()).ownerId(dto.getOwnerId()).publicId(dto.getPublicId()).build()));
    }

    @Override
    public List<AttachmentDTO> getAttachmentsByOwnerId(String ownerId, Pageable pageable) throws AppCheckedException {
        userExists(ownerId);
        return attachmentsRepository.getAttachmentsByOwnerId(ownerId, pageable);
    }

    @Override
    public List<AttachmentDTO> getAttachmentsByType(String ownerId, String fileType, Pageable pageable) throws AppCheckedException {
        userExists(ownerId);
        return attachmentsRepository.getAttachmentsByOwnerId(fileType, pageable);
    }

    public void userExists(String userId) throws AppCheckedException {
        Map<String, Boolean> userExists = accountclient.existsAccountByUserId(userId).getData();
        //check user exists
        if (!userExists.get("exists")) {
            throw new AppCheckedException("User not found", StatusCode.USER_NOT_FOUND);
        }
    }
}
