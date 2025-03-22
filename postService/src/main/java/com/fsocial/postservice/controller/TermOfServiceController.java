package com.fsocial.postservice.controller;

import com.fsocial.postservice.dto.Response;
import com.fsocial.postservice.dto.termOfService.TermOfServiceDTO;
import com.fsocial.postservice.exception.AppCheckedException;
import com.fsocial.postservice.services.TermOfServicesService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping
    public ResponseEntity<Response> updateTermOfSerivce(@RequestBody TermOfServiceDTO termOfServiceDTO) throws AppCheckedException {
        TermOfServiceDTO termOfService = termOfServicesService.updateTermOfService(termOfServiceDTO);
        return ResponseEntity.ok().body(Response.builder()
                .message("Cập nhật chính sách mới thành công")
                .data(termOfService)
                .build());
    }

    @DeleteMapping
    public ResponseEntity<Response> deleteTermOfSerivce(@RequestParam("term_id") String termId) throws AppCheckedException {

        return ResponseEntity.ok().body(Response.builder()
                .message("Cập nhật chính sách mới thành công")
                .data(termOfServicesService.deleteTermOfService(termId))
                .build());
    }
}
