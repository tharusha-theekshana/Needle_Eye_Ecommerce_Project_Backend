package com.needleeye.auth_service.Service;

import com.needleeye.auth_service.Dto.Request.LoginRequestDto;
import com.needleeye.auth_service.Dto.Request.RegisterRequestDto;
import com.needleeye.auth_service.Dto.Response.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<ApiResponse<?>> registerUser(RegisterRequestDto registerRequestData);
    ResponseEntity<ApiResponse<?>> login(LoginRequestDto loginRequestData);
}
