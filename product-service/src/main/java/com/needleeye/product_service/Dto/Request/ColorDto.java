package com.needleeye.product_service.Dto.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class ColorDto {

    @NotBlank(message = "Color name is required")
    private String color;

    @NotBlank(message = "Color code is required")
    @Pattern(regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$", message = "Color code must be a valid hex color (e.g. #FF5733 or #F53)")
    private String colorCode;

    public ColorDto() {
    }

    public ColorDto(String color, String colorCode) {
        this.color = color;
        this.colorCode = colorCode;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }
}
