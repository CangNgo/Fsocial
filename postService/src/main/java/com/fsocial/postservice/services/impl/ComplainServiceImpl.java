package com.fsocial.postservice.services.impl;

import com.fsocial.postservice.dto.complaint.ComplaintDTO;
import com.fsocial.postservice.dto.complaint.ComplaintDTOResponse;
import com.fsocial.postservice.dto.complaint.ComplaintStatisticsDTO;
import com.fsocial.postservice.dto.complaint.ComplaintStatisticsLongDayDTO;
import com.fsocial.postservice.dto.profile.ProfileResponse;
import com.fsocial.postservice.entity.Complaint;
import com.fsocial.postservice.entity.TermOfServices;
import com.fsocial.postservice.exception.AppCheckedException;
import com.fsocial.postservice.exception.StatusCode;
import com.fsocial.postservice.mapper.ComplantMapper;
import com.fsocial.postservice.repository.ComplaintRepository;
import com.fsocial.postservice.repository.TermRepository;
import com.fsocial.postservice.repository.httpClient.ProfileClient;
import com.fsocial.postservice.services.ComplaintService;
import jdk.jshell.Snippet;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ComplainServiceImpl implements ComplaintService {

    ComplaintRepository complaintRepository;

    ComplantMapper complantMapper;

    TermRepository termRepository;

    ProfileClient profileClient;

    @Override
    public ComplaintDTO addComplaint(ComplaintDTO complaint) throws AppCheckedException {
        Complaint complaintentity = complantMapper.toComplaint(complaint);
        Complaint res = complaintRepository.save(complaintentity);
        return complantMapper.toComplaintDTO(res);
    }

    @Override
    public ComplaintDTO readComplaint(String complaintId) throws AppCheckedException {
        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new AppCheckedException("Không tìm thấy khiếu nại", StatusCode.COMPLAIN_NOT_FOUND));
        complaint.setReadding(true);

        return complantMapper.toComplaintDTO(complaintRepository.save(complaint));
    }

    // Methods from timelineService
    @Override
    public List<com.fsocial.postservice.dto.complaint.ComplaintDTOResponse> getComplaints() {
        return complaintRepository.findAll().stream()
                .map(complaint -> {
                    try {
                        return mapToComplainResponse(complaint);
                    } catch (AppCheckedException e) {
                        throw new RuntimeException(e.getMessage());
                    }
                })
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public com.fsocial.postservice.dto.complaint.ComplaintDTOResponse getComplaintById(String complaintId)
            throws AppCheckedException {
        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new AppCheckedException("Không tìm thấy báo cáo", StatusCode.COMPLAIN_NOT_FOUND));
        return mapToComplainResponse(complaint);
    }

    @Override
    public List<com.fsocial.postservice.dto.complaint.ComplaintStatisticsDTO> countStatisticsComplainToday(
            LocalDateTime startDate, LocalDateTime endDate) {
        List<com.fsocial.postservice.dto.complaint.ComplaintStatisticsDTO> complaintStatisticsDTOS = complaintRepository
                .countByCreatedAtByHours(startDate, endDate);
        List<com.fsocial.postservice.dto.complaint.ComplaintStatisticsDTO> result = new java.util.ArrayList<>();
        java.util.Map<String, Integer> mapComplaint = new java.util.HashMap<>();

        for (com.fsocial.postservice.dto.complaint.ComplaintStatisticsDTO complaintStatisticsDTO : complaintStatisticsDTOS) {
            String hour = complaintStatisticsDTO.getHour();
            Integer count = complaintStatisticsDTO.getCount();
            mapComplaint.put(hour, count);
        }

        for (int hour = 0; hour < 24; hour++) {
            if (mapComplaint.containsKey(String.valueOf(hour))) {
                result.add(new com.fsocial.postservice.dto.complaint.ComplaintStatisticsDTO(String.valueOf(hour),
                        mapComplaint.get(String.valueOf(hour))));
            }
            result.add(new com.fsocial.postservice.dto.complaint.ComplaintStatisticsDTO(String.valueOf(hour), 0));
        }

        return result;
    }

    @Override
    public List<com.fsocial.postservice.dto.complaint.ComplaintStatisticsLongDayDTO> countStatisticsComplainLongDay(
            LocalDateTime startDate, LocalDateTime endDate) {
        List<com.fsocial.postservice.dto.complaint.ComplaintStatisticsLongDayDTO> complaintStatisticsDTOS = complaintRepository
                .countByDate(startDate, endDate);
        List<com.fsocial.postservice.dto.complaint.ComplaintStatisticsLongDayDTO> result = new java.util.ArrayList<>();

        LocalDateTime start = startDate.truncatedTo(java.time.temporal.ChronoUnit.DAYS);
        LocalDateTime end = endDate.plusDays(1).truncatedTo(java.time.temporal.ChronoUnit.DAYS);
        while (!start.equals(end)) {
            for (com.fsocial.postservice.dto.complaint.ComplaintStatisticsLongDayDTO complaint : complaintStatisticsDTOS) {
                if (complaint.getDate().truncatedTo(java.time.temporal.ChronoUnit.DAYS).equals(start)) {
                    result.add(complaint);
                    complaintStatisticsDTOS.remove(complaint);
                } else {
                    result.add(new com.fsocial.postservice.dto.complaint.ComplaintStatisticsLongDayDTO(start, 0));
                }
            }

            if (complaintStatisticsDTOS.isEmpty()) {
                result.add(new com.fsocial.postservice.dto.complaint.ComplaintStatisticsLongDayDTO(start, 0));
            }
            start = start.plusDays(1);
        }
        return result;
    }

    private com.fsocial.postservice.dto.complaint.ComplaintDTOResponse mapToComplainResponse(Complaint complaint)
            throws AppCheckedException {
        com.fsocial.postservice.dto.profile.ProfileResponse profileResponse = getProfile(complaint.getUserId());
        TermOfServices term = termRepository.findById(complaint.getTermOfServiceId()).orElseThrow(
                () -> new AppCheckedException("Không tìm thấy chính sách", StatusCode.TERM_OF_SERVICE_NOT_FOUND));
        return com.fsocial.postservice.dto.complaint.ComplaintDTOResponse.builder()
                .id(complaint.getId())
                .postId(complaint.getPostId())
                .profileId(profileResponse.getId())
                .complaintType(complaint.getComplaintType())
                .readding(complaint.isReadding())
                .termOfService(term.getName())
                .createDatetime(complaint.getCreateDatetime())
                .firstName(profileResponse.getFirstName())
                .lastName(profileResponse.getLastName())
                .userId(complaint.getUserId())
                .build();
    }

    public com.fsocial.postservice.dto.profile.ProfileResponse getProfile(String userId) throws AppCheckedException {
        try {
            return profileClient.getProfileResponseByUserId(userId);
        } catch (Exception e) {
            throw new AppCheckedException("Không tìm thấy thông tin người dùng: " + userId, StatusCode.USER_NOT_FOUND);
        }
    }
}
