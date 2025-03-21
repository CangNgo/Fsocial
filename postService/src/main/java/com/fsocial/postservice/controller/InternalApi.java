package com.fsocial.postservice.controller;

import com.fsocial.postservice.exception.AppCheckedException;
import com.fsocial.postservice.services.UploadMedia;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/internal")
@Slf4j
public class InternalApi {
    UploadMedia uploadImage;

    @PostMapping("/upload-file")
    public String uploadFile(@RequestParam("fileUpload") MultipartFile[] file) throws AppCheckedException {
        String[] urlFile = uploadImage.uploadMedia(file);
        log.info("Tải ảnh lên thành công: {}", urlFile[0]);
        return urlFile[0];
    }
}
