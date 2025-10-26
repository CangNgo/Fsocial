package com.fsocial.postservice.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.fsocial.postservice.dto.Attachments.AttachmentDTO;
import com.fsocial.postservice.exception.AppCheckedException;
import com.fsocial.postservice.exception.StatusCode;
import com.fsocial.postservice.services.AttachmentsService;
import com.fsocial.postservice.services.UploadMedia;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UploadMediaImpl implements UploadMedia {
    Cloudinary cloudinary;
    AttachmentsService attachmentsService;
    private static final int BUFFER_SIZE = 8192; // Tăng buffer size để upload nhanh hơn
    private static final String[] SUPPORTED_IMAGE_TYPES = {"jpg", "jpeg", "png", "gif"};
    private static final String[] SUPPORTED_VIDEO_TYPES = {"mp4", "mov", "avi", "wmv"};

    @Override
    public String[] uploadMedia(MultipartFile[] files) throws AppCheckedException {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        if (files == null || files.length == 0) {
            throw new AppCheckedException("No files provided", StatusCode.FILE_NOT_FOUND);
        }

        String[] mediaUrls = new String[files.length];

        for (int i = 0; i < files.length; i++) {
            if (files[i] == null || files[i].isEmpty()) {
                continue;
            }

            File tempFile = null;
            try {
                String[] fileParts = extractFileParts(files[i].getOriginalFilename());
                String publicId = generatePublicId(fileParts[0]);
                String extension = fileParts[1].toLowerCase();
                String resourceType = determineResourceType(extension);

                tempFile = convertToFile(files[i], extension);
                Map<String, Object> uploadParams = configureUploadParams(resourceType);

                uploadParams.put("public_id", publicId);
                uploadParams.put("resource_type", resourceType);
                uploadParams.put("overwrite", true);
                uploadParams.put("invalidate", true);

                Map uploadResult = cloudinary.uploader().upload(tempFile, uploadParams);

                mediaUrls[i] = (String) uploadResult.get("secure_url");
                //save attachments
                attachmentsService.save(AttachmentDTO.builder()
                                .publicId(uploadResult.get("public_id").toString())
                                .size(uploadResult.get("bytes").toString())
                                .resourceType(uploadResult.get("resource_type").toString())
                                .fileType(uploadResult.get("format").toString())
                                .ownerId(userId)
                                .url(uploadResult.get("secure_url").toString())
                        .build());

            } catch (Exception e) {
                log.error("Error uploading file: {}", e.getMessage());
                throw new AppCheckedException("Upload failed: " + e.getMessage(), StatusCode.INTERNAL_SERVER_ERROR);
            } finally {
                cleanUpTempFile(tempFile);
            }
        }
        return mediaUrls;
    }
    private Map<String, Object> configureUploadParams(String resourceType) {
        if ("image".equals(resourceType)) {
            return ObjectUtils.asMap(
                    "fetch_format", "auto",     // Tự động chọn định dạng tối ưu (WebP nếu hỗ trợ)
                    "quality", "80"            // Giới hạn chất lượng ảnh ở 80%
            );
        } else if ("video".equals(resourceType)) {
            return ObjectUtils.asMap(
                    "video_codec", "h264",     // Sử dụng codec H.264
                    "quality", "70"            // Giới hạn chất lượng video ở 70%
            );
        }
        return ObjectUtils.emptyMap(); // Mặc định nếu không xác định được
    }

    private String determineResourceType(String extension) throws AppCheckedException {
        if (Arrays.stream(SUPPORTED_IMAGE_TYPES).anyMatch(extension::equals)) {
            return "image";
        } else if (Arrays.stream(SUPPORTED_VIDEO_TYPES).anyMatch(extension::equals)) {
            return "video";
        }
        throw new AppCheckedException("Unsupported file type: " + extension, StatusCode.UNSUPPORTED_MEDIA_TYPE);
    }

    private File convertToFile(MultipartFile file, String extension) throws IOException {
        File tempFile = File.createTempFile(UUID.randomUUID().toString(), "." + extension);

        try (InputStream in = file.getInputStream();
             FileOutputStream out = new FileOutputStream(tempFile)) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
        return tempFile;
    }

    private String generatePublicId(String fileName) {
        return "media_" + UUID.randomUUID().toString() + "_" + fileName;
    }

    private String[] extractFileParts(String originalName) throws AppCheckedException {
        if (originalName == null || !originalName.contains(".")) {
            throw new AppCheckedException("Invalid filename", StatusCode.UNSUPPORTED_MEDIA_TYPE);
        }
        String[] parts = originalName.split("\\.");
        return new String[]{parts[0], parts[parts.length - 1]};
    }

    private void cleanUpTempFile(File file) {
        if (file != null && file.exists()) {
            try {
                Files.delete(file.toPath());
            } catch (IOException e) {
                log.warn("Failed to delete temporary file: {}", e.getMessage());
            }
        }
    }
}