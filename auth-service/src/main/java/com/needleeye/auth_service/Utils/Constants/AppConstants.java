package com.needleeye.auth_service.Utils.Constants;

public class AppConstants {
    public static final String SERVER_ERROR = "Internal server error.";
    public static final String EMAIL_EXISTS = "Email already taken. Please use another one.";
    public static final String INVALID_CREDENTIALS = "Invalid email or password.";
    public static final String USER_REG_SUCCESS = "User registration success.";
    public static final String USER_REG_FAIL = "User registration failed.";
    public static final String USER_LOGIN_SUCCESS = "User login success.";
    public static final String USER_NOT_FOUND = "User not found.";
    public static final String OTP_SENT_SUCCESS = "OTP sent to your registered email.";
    public static final String OTP_INVALID = "Invalid verification code.";
    public static final String OTP_EXPIRED = "Verification code has expired, request a new one.";
    public static final String OTP_VERIFICATION_SUCCESS = "Code verification success.";
    public static final String PASSWORD_MUST_DIFFERENT = "New password cannot be the current password.";
    public static final String PASSWORD_RESET_SUCCESS = "Password reset successful.";
    public static final String OLD_PASSWORD_INVALID = "Old password is incorrect.";
    public static final String PASSWORD_CHANGE_SUCCESS = "Password changed successfully.";
    public static final int OTP_EXPIRY_MINUTES = 5;
}
