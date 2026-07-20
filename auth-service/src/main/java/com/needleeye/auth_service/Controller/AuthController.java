package com.needleeye.auth_service.Controller;

import com.needleeye.auth_service.Dto.Request.*;
import com.needleeye.auth_service.Dto.Response.ApiResponse;
import com.needleeye.auth_service.Service.AuthService;
import com.needleeye.auth_service.Utils.Constants.AppConstants;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    ResponseEntity<ApiResponse<?>> registerUser(@Valid @RequestBody RegisterRequestDto registerRequestData){
        try {
            return authService.registerUser(registerRequestData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.SERVER_ERROR));
    }

    @PostMapping("/login")
    ResponseEntity<ApiResponse<?>> login(@Valid @RequestBody LoginRequestDto loginRequestData){
        try {
            return authService.login(loginRequestData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.SERVER_ERROR));
    }

    @PostMapping("/forgot-password")
    ResponseEntity<ApiResponse<?>> forgotPassword(@Valid @RequestBody ForgotPasswordRequestDto forgotPasswordRequestData){
        try {
            return authService.forgotPassword(forgotPasswordRequestData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.SERVER_ERROR));
    }

    @PostMapping("/otp-verification")
    ResponseEntity<ApiResponse<?>> otpVerification(@Valid @RequestBody OtpVerificationDto otpVerificationDto){
        try {
            return authService.otpVerification(otpVerificationDto);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.SERVER_ERROR));
    }

    @PostMapping("/reset-password")
    ResponseEntity<ApiResponse<?>> resetPassword(@Valid @RequestBody ResetPasswordRequestDto resetPasswordRequestData){
        try {
            return authService.resetPassword(resetPasswordRequestData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.SERVER_ERROR));
    }
}
