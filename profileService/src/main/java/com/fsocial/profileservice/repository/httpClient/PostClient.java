package com.fsocial.profileservice.repository.httpClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "postService", url = "${app.services.post}")
public interface PostClient {

    @PostMapping(value = "/internal/upload-file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    String uploadFile(@RequestPart("fileUpload") MultipartFile[] file);
}
