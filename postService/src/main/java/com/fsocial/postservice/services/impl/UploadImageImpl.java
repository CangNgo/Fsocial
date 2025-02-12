package com.fsocial.postservice.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.fsocial.postservice.exception.AppCheckedException;
import com.fsocial.postservice.exception.StatusCode;
import com.fsocial.postservice.services.UploadImage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UploadImageImpl implements UploadImage {
    Cloudinary cloudinary;

    @Override
    public String[] uploadImage(MultipartFile[] files) throws AppCheckedException, IOException {
        String[] urlImages = new String[files.length];

        for (int i = 0; i < files.length; i++) {
            File fileUpload = null;
            try {
                String publicValue = generatePublicValue(files[i].getOriginalFilename());
                String extension = getFileName(Objects.requireNonNull(files[i].getOriginalFilename()))[1];

                fileUpload = convert(files[i]);

                cloudinary.uploader().upload(fileUpload, ObjectUtils.asMap(
                        "public_id", publicValue,
                        "resource_type", "auto"
                ));

                urlImages[i] = cloudinary.url().generate(publicValue + "." + extension);

            } finally {
                if (fileUpload != null && fileUpload.exists()) {
                    cleanDisk(fileUpload);
                }
            }
        }
        return urlImages;
    }

    private void cleanDisk(File file) throws AppCheckedException {
        try {
            Files.deleteIfExists(file.toPath());
        } catch (IOException e) {
            throw new AppCheckedException("File deleted not found", StatusCode.FILE_NOT_FOUND);
        }
    }

    public File convert(MultipartFile file) throws IOException {
        File tempFile = File.createTempFile(
                UUID.randomUUID().toString(),
                "." + getFileName(file.getOriginalFilename())[1]
        );

        try (InputStream in = file.getInputStream();
             OutputStream out = new FileOutputStream(tempFile)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            out.flush();
        }

        return tempFile;
    }

    public String generatePublicValue(String originalFileName) {
        String fileName = getFileName(originalFileName)[0];
        return UUID.randomUUID().toString() + "_" + fileName;
    }

    public String[] getFileName(String originalName) {
        return originalName.split("\\.");
    }
}