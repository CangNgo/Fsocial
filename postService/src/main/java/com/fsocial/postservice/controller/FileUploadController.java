package com.fsocial.postservice.controller;

import com.fsocial.postservice.dto.ApiResponse;
import com.fsocial.postservice.dto.Response;
import com.fsocial.postservice.enums.ResponseStatus;
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

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/upload_file")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class FileUploadController {
    UploadMedia uploadImage;

    @PostMapping
    public ResponseEntity<Response> uploadFile(@RequestParam("fileUpload") MultipartFile[] file) throws AppCheckedException {

            String[] urlfile = uploadImage.uploadMedia(file);

            log.info("Upload file successfull: {}", (Object) urlfile);
            return ResponseEntity.ok().body(Response.builder()
                            .data(urlfile)
                            .message("Upload file successful")
                            .dateTime(LocalDateTime.now())
                    .build());

    }

    @PostMapping("/messages")
    public ApiResponse<List<String>> uploadImageInMessage(@RequestParam MultipartFile[] images) throws AppCheckedException {
        String[] urlfile = uploadImage.uploadMedia(images);
        return ApiResponse.buildApiResponse(Arrays.asList(urlfile), ResponseStatus.SUCCESS);
    }
}
