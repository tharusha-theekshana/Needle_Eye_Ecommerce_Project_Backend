package com.needleeye.auth_service.Dto.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ChangePasswordRequestDto {
    @NotBlank(message = "User id is required")
    private String userId;

    @NotBlank(message = "Old password is required")
    private String oldPassword;

    @NotBlank(message = "New password is required")
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*\\d).+$",
            message = "Password must contain at least one uppercase letter and one number"
    )
    private String newPassword;

    public ChangePasswordRequestDto() {
    }

    public ChangePasswordRequestDto(String userId, String oldPassword, String newPassword) {
        this.userId = userId;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
