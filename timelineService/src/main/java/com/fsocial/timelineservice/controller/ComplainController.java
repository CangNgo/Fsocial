package com.fsocial.timelineservice.controller;

import com.fsocial.timelineservice.dto.Response;
import com.fsocial.timelineservice.dto.complaint.ComplaintDTO;
import com.fsocial.timelineservice.mapper.ComplantMapper;
import com.fsocial.timelineservice.services.ComplaintService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/complaint")
@Slf4j
public class ComplainController {

    ComplaintService complaintService;
    ComplantMapper complantMapper;


    @GetMapping
    public ResponseEntity<Response> getComplaints() {

            return ResponseEntity.ok().body(Response.builder()
                            .data(complaintService.getComplaints())
                            .message("Lấy toàn bộ danh sách báo cáo thành công")
                    .build());
    }
}
