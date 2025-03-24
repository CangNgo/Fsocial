package com.fsocial.postservice.services.impl;

import com.fsocial.postservice.dto.complaint.ComplaintDTO;
import com.fsocial.postservice.entity.Complaint;
import com.fsocial.postservice.entity.TermOfServices;
import com.fsocial.postservice.exception.AppCheckedException;
import com.fsocial.postservice.exception.StatusCode;
import com.fsocial.postservice.mapper.ComplantMapper;
import com.fsocial.postservice.repository.ComplaintRepository;
import com.fsocial.postservice.repository.TermRepository;
import com.fsocial.postservice.services.ComplaintService;
import jdk.jshell.Snippet;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ComplainServiceImpl implements ComplaintService {

    ComplaintRepository complaintRepository;

    ComplantMapper complantMapper;

    TermRepository termRepository;

    @Override
    public ComplaintDTO addComplaint(ComplaintDTO complaint) throws AppCheckedException {
        Complaint complaintentity = complantMapper.toComplaint(complaint);
        Complaint res = complaintRepository.save(complaintentity);
        return complantMapper.toComplaintDTO(res);
    }

    @Override
    public ComplaintDTO readComplaint(String complaintId) throws AppCheckedException {
        Complaint complaint = complaintRepository.findById(complaintId).orElseThrow(()-> new AppCheckedException("Không tìm thấy khiếu nại", StatusCode.COMPLAIN_NOT_FOUND));
        complaint.setReadding(true);

        return complantMapper.toComplaintDTO(complaintRepository.save(complaint));
    }
}
