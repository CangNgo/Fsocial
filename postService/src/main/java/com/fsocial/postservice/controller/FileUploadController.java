package com.fsocial.postservice.controller;

import com.fsocial.postservice.dto.Response;
import com.fsocial.postservice.exception.AppCheckedException;
import com.fsocial.postservice.services.UploadMedia;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/upload_file")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class FileUploadController {
    UploadMedia uploadImage;

    @PostMapping
    public ResponseEntity<ApiResponse> uploadFile(@RequestParam("fileUpload") MultipartFile[] file) {
        try {
            String[] urlfile = uploadImage.uploadMedia(file);

            log.info("Upload file successfull: {}", (Object) urlfile);
            return ResponseEntity.ok().body(ApiResponse.builder()
                            .data(urlfile)
                            .message("Upload file successful")
                            .dateTime(LocalDateTime.now())
                    .build());
        } catch (IOException| AppCheckedException e) {
            log.error("Lỗi khi upload file: {}", e.getMessage());
            return ResponseEntity.ok().body(ApiResponse.builder()
                    .data(null)
                    .message("Upload file fail")
                    .dateTime(LocalDateTime.now())
                    .build());
        }
    }
}
