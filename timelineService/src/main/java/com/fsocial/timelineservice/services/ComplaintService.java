package com.fsocial.timelineservice.services;

import com.fsocial.timelineservice.dto.complaint.ComplaintDTO;
import com.fsocial.timelineservice.dto.complaint.ComplaintDTOResponse;
import com.fsocial.timelineservice.dto.complaint.ComplaintStatisticsDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface ComplaintService {
    ComplaintDTO addComplaint(ComplaintDTO complaint);
    List<ComplaintDTOResponse> getComplaints();

    List<ComplaintStatisticsDTO> countComplaintByToday(LocalDateTime startDate, LocalDateTime endDate);
}
