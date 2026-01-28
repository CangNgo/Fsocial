package com.fsocial.postservice.services;

import com.fsocial.postservice.dto.complaint.ComplaintDTO;
import com.fsocial.postservice.dto.complaint.ComplaintDTOResponse;
import com.fsocial.postservice.dto.complaint.ComplaintStatisticsDTO;
import com.fsocial.postservice.dto.complaint.ComplaintStatisticsLongDayDTO;
import com.fsocial.postservice.exception.AppCheckedException;

import java.util.List;

public interface ComplaintService {
    ComplaintDTO addComplaint(ComplaintDTO complaint) throws AppCheckedException;
    ComplaintDTO readComplaint(String complaintId) throws AppCheckedException;

    // Methods from timelineService
    List<ComplaintDTOResponse> getComplaints();
    ComplaintDTOResponse getComplaintById(String complaintId) throws AppCheckedException;
    List<ComplaintStatisticsDTO> countStatisticsComplainToday(java.time.LocalDateTime startDate, java.time.LocalDateTime endDate);
    List<ComplaintStatisticsLongDayDTO> countStatisticsComplainLongDay(java.time.LocalDateTime startDate, java.time.LocalDateTime endDate);
}
