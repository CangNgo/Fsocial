package com.fsocial.postservice.repository;

import com.fsocial.postservice.dto.Attachments.AttachmentDTO;
import com.fsocial.postservice.entity.Attachments;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttachmentsRepository extends MongoRepository<Attachments, String> {
    List<AttachmentDTO> getAttachmentsByOwnerId(String ownerId, Pageable pageable);
    List<AttachmentDTO> getAttachmentsByFileType(String fileType, Pageable pageable);
}
