package com.needleeye.product_service.Service.Impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.needleeye.product_service.Dto.Response.ApiResponse;
import com.needleeye.product_service.Dto.Response.ImageUploadResponseDto;
import com.needleeye.product_service.Service.CloudinaryService;
import com.needleeye.product_service.Utils.Constants.AppConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {

    private final Cloudinary cloudinary;

    @Value("${cloudinary.folder}")
    private String cloudinaryFolder;


    public CloudinaryServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public ImageUploadResponseDto uploadImage(MultipartFile file) {
        try {
            validateImage(file);

            Map<?, ?> result = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", cloudinaryFolder,
                            "resource_type", "image"
                    )
            );

            String imageUrl = (String) result.get("secure_url");
            String imageId = (String) result.get("public_id");

            return new ImageUploadResponseDto(imageUrl, imageId);

        } catch (IOException e) {
            throw new RuntimeException("Image upload to Cloudinary failed: " + e.getMessage());
        }
    }

    @Override
    public void deleteImage(String imageUrl) {
        try {
            String publicId = extractPublicId(imageUrl);
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());

        } catch (IOException e) {
            throw new RuntimeException("Image deletion from Cloudinary failed: " + e.getMessage());
        }
    }

    @Override
    public String extractPublicId(String imageUrl) {
        int uploadIndex = imageUrl.indexOf("/upload/");
        String afterUpload = imageUrl.substring(uploadIndex + 8);
        int versionEnd = afterUpload.indexOf("/");
        String withFolder = afterUpload.substring(versionEnd + 1);
        int dotIndex = withFolder.lastIndexOf(".");
        return dotIndex != -1 ? withFolder.substring(0, dotIndex) : withFolder;

    }

    // Validate image
    private ResponseEntity<ApiResponse<?>> validateImage(MultipartFile image) {
        if (image.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), AppConstants.IMAGE_REQUIRED));
        }

        String contentType = image.getContentType();
        List<String> allowed = List.of("image/jpeg", "image/png", "image/webp");

        if (contentType == null || !allowed.contains(contentType)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), AppConstants.BAD_IMAGE_FORMAT));
        }

        long maxSize = 5 * 1024 * 1024;
        if (image.getSize() > maxSize) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), AppConstants.IMAGE_SIZE_INVALID));
        }
        return null;
    }
}
