package com.fsocial.postservice.services;

import com.fsocial.postservice.dto.complaint.ComplaintDTO;

import java.util.List;

public interface ComplaintService {
    ComplaintDTO addComplaint(ComplaintDTO complaint);
    List<ComplaintDTO> getComplaints();
}
