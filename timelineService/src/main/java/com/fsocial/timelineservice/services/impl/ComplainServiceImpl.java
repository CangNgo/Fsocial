package com.fsocial.timelineservice.services.impl;

import com.fsocial.timelineservice.dto.complaint.ComplaintDTO;
import com.fsocial.timelineservice.mapper.ComplantMapper;
import com.fsocial.timelineservice.repository.ComplaintRepository;
import com.fsocial.timelineservice.services.ComplaintService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ComplainServiceImpl implements ComplaintService {

    ComplaintRepository complaintRepository;

    ComplantMapper complantMapper;

    @Override
    public ComplaintDTO addComplaint(ComplaintDTO complaint) {
        return complantMapper.toComplaintDTO(
                complaintRepository.save(complantMapper.toComplaint(complaint)));
    }

    @Override
    public List<ComplaintDTO> getComplaints() {
        return complantMapper.toComplaintDTO(complaintRepository.findAll());
    }
}
