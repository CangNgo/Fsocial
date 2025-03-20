package com.fsocial.timelineservice.controller;

import com.fsocial.timelineservice.dto.Response;
import com.fsocial.timelineservice.dto.complaint.ComplaintDTO;
import com.fsocial.timelineservice.dto.complaint.ComplaintStatisticsDTO;
import com.fsocial.timelineservice.exception.AppCheckedException;
import com.fsocial.timelineservice.mapper.ComplantMapper;
import com.fsocial.timelineservice.services.ComplaintService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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

    @GetMapping("/{complaint_id}")
    public ResponseEntity<Response> getComplaint(@PathVariable("complaint_id") String complaintId) throws AppCheckedException {

        return ResponseEntity.ok().body(Response.builder()
                .data(complaintService.getComplaintById(complaintId))
                .message("Lấy toàn bộ danh sách báo cáo thành công")
                .build());
    }

    @GetMapping("/statistics_complaint_today")
    public ResponseEntity<Response> getComplaintStatistics(@RequestParam("date_time" )String dateTime) {
        LocalDate date = LocalDate.parse(dateTime);
        LocalDateTime startDate = date.atStartOfDay();
        LocalDateTime endDate = date.atTime(23, 59, 59);

        return ResponseEntity.ok().body(Response.builder()
                .data(complaintService.countStatisticsComplainToday(startDate, endDate))
                .message("Lấy toàn bộ danh sách báo cáo thành công")
                .build());
    }

    @GetMapping("/statistics_complaint_start_end")
    public ResponseEntity<Response> getComplaintStatistics(@RequestParam("startDate" )String startDateRe,@RequestParam("endDate" )String endDateRe ) {
        LocalDate start = LocalDate.parse(startDateRe);
        LocalDate end = LocalDate.parse(endDateRe);
        LocalDateTime startDate= start.atStartOfDay();
        LocalDateTime endDate= end.atTime(23, 59, 59);

        return ResponseEntity.ok().body(Response.builder()
                .data(complaintService.countStatisticsComplainToday(startDate, endDate))
                .message("Lấy toàn bộ danh sách báo cáo thành công")
                .build());
    }
}
