package com.fsocial.postservice.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.fsocial.postservice.services.UploadImage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UploadImageImpl implements UploadImage {
    Cloudinary cloudinary;
    @Override
    public String uploadImage(MultipartFile file) throws IOException {
        assert file != null;
        String generateValue = generatePublicValue(file.getOriginalFilename());

        cloudinary.uploader().upload("my_picture.jpg", ObjectUtils.emptyMap());
        return null;
    }

    public File convert (MultipartFile file) throws IOException {
        assert file != null;
        File convFile = new File(StringUtils.join(generatePublicValue(file.getOriginalFilename()),
                getFileName(Objects.requireNonNull(file.getOriginalFilename()))[1]));
        try(InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, convFile.toPath());
        }

        return convFile;
    }

    public String generatePublicValue(String originalFileName) {
        String fileName = getFileName(originalFileName)[0];
        return StringUtils.join(UUID.randomUUID().toString() ,"_", fileName);
    }

    public String[] getFileName(String originalName) {
        return originalName.split("\\.");
    }
}
