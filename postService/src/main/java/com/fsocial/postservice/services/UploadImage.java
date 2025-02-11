package com.fsocial.postservice.services;

import com.fsocial.postservice.exception.AppCheckedException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UploadImage {
    String[] uploadImage(MultipartFile[] file) throws AppCheckedException,IOException;
}
