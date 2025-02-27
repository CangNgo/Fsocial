package com.fsocial.timelineservice.services;

import com.fsocial.timelineservice.dto.complaint.ComplaintDTO;

import java.util.List;

public interface ComplaintService {
    ComplaintDTO addComplaint(ComplaintDTO complaint);
    List<ComplaintDTO> getComplaints();
}
