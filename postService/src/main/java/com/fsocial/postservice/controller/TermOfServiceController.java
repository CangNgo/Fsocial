package com.fsocial.postservice.controller;

import com.fsocial.postservice.dto.Response;
import com.fsocial.postservice.dto.termOfService.TermOfServiceDTO;
import com.fsocial.postservice.entity.TermOfServices;
import com.fsocial.postservice.services.TermOfServicesService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/term_of_service")
public class TermOfServiceController {

    TermOfServicesService termOfServicesService;

    @PostMapping
    public ResponseEntity<Response> addTermOfSerivce(@RequestBody TermOfServiceDTO termOfServiceDTO) {
        TermOfServiceDTO termOfService = termOfServicesService.addTermOfService(termOfServiceDTO);
        return ResponseEntity.ok().body(Response.builder()
                        .message("Thêm chính sách mới thành công")
                        .data(termOfService)
                .build());
    }
}
