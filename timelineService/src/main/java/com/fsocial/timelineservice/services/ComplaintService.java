package com.fsocial.timelineservice.services;

import com.fsocial.timelineservice.dto.complaint.ComplaintDTO;
import com.fsocial.timelineservice.dto.complaint.ComplaintDTOResponse;
import com.fsocial.timelineservice.dto.complaint.ComplaintStatisticsDTO;
import com.fsocial.timelineservice.dto.complaint.ComplaintStatisticsLongDayDTO;
import com.fsocial.timelineservice.dto.post.PostStatisticsDTO;
import com.fsocial.timelineservice.dto.post.PostStatisticsLongDateDTO;
import com.fsocial.timelineservice.exception.AppCheckedException;

import java.time.LocalDateTime;
import java.util.List;

public interface ComplaintService {
    ComplaintDTO addComplaint(ComplaintDTO complaint);
    List<ComplaintDTOResponse> getComplaints();
    ComplaintDTOResponse getComplaintById(String complaintId) throws AppCheckedException;
    List<ComplaintStatisticsDTO> countStatisticsComplainToday(LocalDateTime startDate, LocalDateTime endDate);
    List<ComplaintStatisticsLongDayDTO> countStatisticsComplainLongDay(LocalDateTime startDate, LocalDateTime endDate);
}
