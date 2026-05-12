package com.needleeye.auth_service.Dto.Response;

public class LoginResponseDto {
    private String userId;
    private int loginAttempts;
    private String role;
    private String token;

    public LoginResponseDto() {
    }

    public LoginResponseDto(String userId, int loginAttempts, String role, String token) {
        this.userId = userId;
        this.loginAttempts = loginAttempts;
        this.role = role;
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getLoginAttempts() {
        return loginAttempts;
    }

    public void setLoginAttempts(int loginAttempts) {
        this.loginAttempts = loginAttempts;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
