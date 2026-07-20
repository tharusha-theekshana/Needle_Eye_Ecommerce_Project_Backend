package com.needleeye.order_service.Configuration.OpenFeign;

import com.needleeye.order_service.Dto.Response.ApiResponse;
import com.needleeye.order_service.Dto.Response.UserResponseDataDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "USER-SERVICE")
public interface UserServiceClient {
    @GetMapping("/api/v1/user/{userId}")
    ResponseEntity<ApiResponse<UserResponseDataDto>> getUserDataById(
            @PathVariable String userId);
}
