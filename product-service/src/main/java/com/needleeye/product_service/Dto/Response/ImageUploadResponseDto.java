package com.needleeye.product_service.Dto.Response;

public class ImageUploadResponseDto {

    private String imageUrl;
    private String imageId;

    public ImageUploadResponseDto() {
    }

    public ImageUploadResponseDto(String imageUrl, String imageId) {
        this.imageUrl = imageUrl;
        this.imageId = imageId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }
}
