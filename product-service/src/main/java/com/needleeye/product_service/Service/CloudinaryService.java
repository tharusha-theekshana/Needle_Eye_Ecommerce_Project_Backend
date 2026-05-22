package com.needleeye.product_service.Service;

import com.needleeye.product_service.Dto.Response.ImageUploadResponseDto;
import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryService {
    public ImageUploadResponseDto uploadImage(MultipartFile file);
    public void deleteImage(String imageUrl);
    String extractPublicId(String imageUrl);

}
