package com.needleeye.auth_service.Configuration.OpenFeign;

import com.needleeye.auth_service.Dto.Request.RegisterRequestDto;
import com.needleeye.auth_service.Dto.Response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "USER-SERVICE", url = "http://localhost:8082/api/v1/user")
public interface UserServiceClient {

    @PostMapping("/save-user-data")
    ResponseEntity<ApiResponse<?>> saveUserData(
            @RequestBody RegisterRequestDto registerRequestDto);
}

