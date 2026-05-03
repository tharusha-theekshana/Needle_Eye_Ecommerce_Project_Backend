package com.needleeye.user_service.Controller;

import com.needleeye.user_service.Dto.Request.RegisterRequestDto;
import com.needleeye.user_service.Dto.Response.ApiResponse;
import com.needleeye.user_service.Service.UserService;
import com.needleeye.user_service.Utils.Constants.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/save-user-data")
    ResponseEntity<ApiResponse<?>> saveUserData(@RequestBody RegisterRequestDto userData){
        try{
            return userService.saveUserData(userData);
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.SERVER_ERROR));
    }
}
