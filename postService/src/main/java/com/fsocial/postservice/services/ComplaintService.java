package com.fsocial.postservice.services;

import com.fsocial.postservice.dto.complaint.ComplaintDTO;

public interface ComplaintService {
    ComplaintDTO addComplaint(ComplaintDTO complaint);
}
