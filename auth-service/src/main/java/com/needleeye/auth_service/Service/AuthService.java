package com.needleeye.auth_service.Service;

import com.needleeye.auth_service.Dto.Request.*;
import com.needleeye.auth_service.Dto.Response.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<ApiResponse<?>> registerUser(RegisterRequestDto registerRequestData);
    ResponseEntity<ApiResponse<?>> login(LoginRequestDto loginRequestData);
    ResponseEntity<ApiResponse<?>> forgotPassword(ForgotPasswordRequestDto forgotPasswordRequestData);
    ResponseEntity<ApiResponse<?>> otpVerification(OtpVerificationDto otpVerificationDto);
    ResponseEntity<ApiResponse<?>> resetPassword(ResetPasswordRequestDto resetPasswordRequestData);
    ResponseEntity<ApiResponse<?>> changePassword(ChangePasswordRequestDto changePasswordRequestData);
}
