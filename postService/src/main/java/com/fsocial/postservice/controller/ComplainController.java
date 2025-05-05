package com.fsocial.postservice.controller;

import com.fsocial.postservice.dto.Response;
import com.fsocial.postservice.dto.complaint.ComplaintDTO;
import com.fsocial.postservice.exception.AppCheckedException;
import com.fsocial.postservice.mapper.ComplantMapper;
import com.fsocial.postservice.services.ComplaintService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/complaint")
@Slf4j
public class ComplainController {

    ComplaintService complaintService;

    @PostMapping
    public ResponseEntity<Response> addComplaint(@RequestBody @Valid ComplaintDTO complaint) throws AppCheckedException {
        ComplaintDTO complaintDTO = complaintService.addComplaint(complaint);
            return ResponseEntity.ok().body(Response.builder()
                            .data(complaintDTO)
                            .message("Báo cáo bài viết thành công")
                            .dateTime(LocalDateTime.now())
                    .build());
    }

    @PutMapping("/reading")
    public ResponseEntity<Response> updateComplaint(@RequestParam("complaint_id") String complaintId) throws AppCheckedException {
        return ResponseEntity.ok().body(Response.builder()
                .data(complaintService.readComplaint(complaintId))
                .message("Cập nhật trạng thái đã đọc báo cáo thành công")
                .build());
    }

}
