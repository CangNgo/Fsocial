package com.fsocial.timelineservice.mapper;

import com.fsocial.timelineservice.dto.complaint.ComplaintDTO;
import com.fsocial.timelineservice.entity.Complaint;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ComplantMapper {
    ComplaintDTO toComplaintDTO(Complaint complaint);
    Complaint toComplaint(ComplaintDTO complaintDTO);
    List<ComplaintDTO> toComplaintDTO(List<Complaint> complaints);
}
