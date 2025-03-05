package com.fsocial.timelineservice.controller;

import com.fsocial.timelineservice.dto.Response;
import com.fsocial.timelineservice.dto.termOfService.TermOfServiceDTO;
import com.fsocial.timelineservice.services.TermOfServiceService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/term_of_service")
public class TermOfServiceController {

    TermOfServiceService termOfServiceService;

    @GetMapping
    public ResponseEntity<Response> getTermOfService() {

        List<TermOfServiceDTO> res =termOfServiceService.getTermOfServices();

        return ResponseEntity.ok().body(Response.builder()
                        .data(res)
                        .message("Lấy toàn bộ danh sách chính sách thành công")
                .build());
    }
}
