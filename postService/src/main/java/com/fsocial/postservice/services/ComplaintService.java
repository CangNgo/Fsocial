package com.fsocial.postservice.services;

import com.fsocial.postservice.dto.complaint.ComplaintDTO;
import com.fsocial.postservice.exception.AppCheckedException;

import java.util.List;

public interface ComplaintService {
    ComplaintDTO addComplaint(ComplaintDTO complaint) throws AppCheckedException;
    ComplaintDTO readComplaint(String complaintId) throws AppCheckedException;
}
