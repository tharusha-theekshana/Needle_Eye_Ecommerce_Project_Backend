package com.needleeye.user_service.Service.Impl;

import com.needleeye.user_service.Dto.Request.RegisterRequestDto;
import com.needleeye.user_service.Dto.Response.ApiResponse;
import com.needleeye.user_service.Entity.User;
import com.needleeye.user_service.Repository.UserRepo;
import com.needleeye.user_service.Service.UserService;
import com.needleeye.user_service.Utils.Constants.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {

    private UserRepo userRepo;

    public UserServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public ResponseEntity<ApiResponse<?>> saveUserData(RegisterRequestDto userData) {
        try {
            User mappedUser = mapDataToUserEntity(userData);
            userRepo.save(mappedUser);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ApiResponse<>(HttpStatus.OK.value(), AppConstants.USER_REG_SUCCESS));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.SERVER_ERROR));
    }

    User mapDataToUserEntity(RegisterRequestDto requestData) {
        User user = new User();

        user.setUserId(requestData.getUserId());
        user.setFirstName(requestData.getFirstName());
        user.setLastName(requestData.getLastName());
        user.setAge(requestData.getAge());
        user.setEmail(requestData.getEmail());
        user.setMobileNumber(requestData.getMobileNumber());
        user.setAddress(requestData.getAddress());
        user.setUserRole(requestData.getUserRole());
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        return user;
    }
}
