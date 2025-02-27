package com.fsocial.postservice.mapper;

import com.fsocial.postservice.dto.complaint.ComplaintDTO;
import com.fsocial.postservice.entity.Complaint;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ComplantMapper {
    ComplaintDTO toComplaintDTO(Complaint complaint);
    Complaint toComplaint(ComplaintDTO complaintDTO);
}
