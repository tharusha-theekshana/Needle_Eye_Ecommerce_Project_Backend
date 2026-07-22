package com.needleeye.product_service.Configuration.OpenFeign;

import com.needleeye.product_service.Dto.Response.ApiResponse;
import com.needleeye.product_service.Dto.Response.UserResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "USER-SERVICE")
public interface UserServiceClient {

    @GetMapping("/api/v1/user/{userId}")
    ResponseEntity<ApiResponse<UserResponseDto>> getUserDataById(
            @PathVariable String userId);
}
