package com.fsocial.postservice.services;

import com.fsocial.postservice.exception.AppCheckedException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UploadMedia {
    String[] uploadMedia(MultipartFile[] file) throws AppCheckedException,IOException;
}
