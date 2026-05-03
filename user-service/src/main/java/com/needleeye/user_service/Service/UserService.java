package com.needleeye.user_service.Service;

import com.needleeye.user_service.Dto.Request.RegisterRequestDto;
import com.needleeye.user_service.Dto.Response.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<ApiResponse<?>> saveUserData(RegisterRequestDto userData);
}
