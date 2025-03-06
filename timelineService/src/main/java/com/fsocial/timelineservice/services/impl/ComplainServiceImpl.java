package com.fsocial.timelineservice.services.impl;

import com.fsocial.timelineservice.dto.complaint.ComplaintDTO;
import com.fsocial.timelineservice.dto.complaint.ComplaintDTOResponse;
import com.fsocial.timelineservice.dto.profile.ProfileResponse;
import com.fsocial.timelineservice.entity.Complaint;
import com.fsocial.timelineservice.entity.TermOfServices;
import com.fsocial.timelineservice.enums.StatusCode;
import com.fsocial.timelineservice.exception.AppCheckedException;
import com.fsocial.timelineservice.exception.AppUnCheckedException;
import com.fsocial.timelineservice.mapper.ComplantMapper;
import com.fsocial.timelineservice.repository.ComplaintRepository;
import com.fsocial.timelineservice.repository.TermOfServicesRepository;
import com.fsocial.timelineservice.repository.httpClient.AccountClient;
import com.fsocial.timelineservice.repository.httpClient.ProfileClient;
import com.fsocial.timelineservice.services.ComplaintService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.metrics.Stat;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ComplainServiceImpl implements ComplaintService {

    ComplaintRepository complaintRepository;

    ComplantMapper complantMapper;

    ProfileClient profileClient;

    AccountClient accountClient;

    TermOfServicesRepository termOfServicesRepository;

    @Override
    public ComplaintDTO addComplaint(ComplaintDTO complaint) {
        return complantMapper.toComplaintDTO(
                complaintRepository.save(complantMapper.toComplaint(complaint)));
    }

    @Override
    public List<ComplaintDTOResponse> getComplaints() {
        return complaintRepository.findAll().stream()
                .map(complaint -> {
                    try {
                        return this.mapToComplainResponse(complaint);
                    }catch (AppCheckedException e) {
                        throw new RuntimeException(e.getMessage() );
                    }
                })
                .collect(Collectors.toList());
    }

    private ComplaintDTOResponse mapToComplainResponse(Complaint complaint) throws AppCheckedException {
        ProfileResponse profileResponse = getProfile(complaint.getUserId());
        TermOfServices term = termOfServicesRepository.findById(complaint.getTermOfServiceId()).orElseThrow(
                ()-> new AppCheckedException("Không tìm thấy chính sách", StatusCode.TERMOFSERVICE_NOT_FOUND)
        );
        return ComplaintDTOResponse.builder()
                .id(complaint.getId())
                .postId(complaint.getPostId())
                .profileId(profileResponse.getId())
                .complaintType(complaint.getComplaintType())
                .reading(complaint.isReading())
                .termOfService(term.getName())
                .dateTime(complaint.getDateTime())
                .firstName(profileResponse.getFirstName())
                .lastName(profileResponse.getLastName())
                .userId(complaint.getUserId())
                .build();
    }

    public ProfileResponse getProfile(String userId) throws AppCheckedException {
        try {
            return profileClient.getProfileResponseByUserId(userId);
        } catch (Exception e) {
            throw new AppCheckedException("Không tìm thấy thông tin người dùng: " + userId, StatusCode.USER_NOT_FOUND);
        }
    }

}
