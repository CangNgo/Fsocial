package com.fsocial.postservice.controller;

import com.fsocial.postservice.dto.Response;
import com.fsocial.postservice.dto.complaint.ComplaintDTO;
import com.fsocial.postservice.mapper.ComplantMapper;
import com.fsocial.postservice.services.ComplaintService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/complaint")
@Slf4j
public class ComplainController {

    ComplaintService complaintService;
    ComplantMapper complantMapper;


    @PostMapping
    public ResponseEntity<Response> addComplaint(@RequestBody @Valid ComplaintDTO complaint) {

            return ResponseEntity.ok().body(Response.builder()
                            .data(complaintService.addComplaint(complaint))
                            .message("Thêm chính sách thành công")
                            .dateTime(LocalDateTime.now())
                    .build());
    }
}
