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
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;

    public UserServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    // Get user data
    @Override
    public ResponseEntity<ApiResponse<?>> getUserDataById(String userId) {
        try {
            Optional<User> userData = userRepo.findByUserId(userId);

            if(userData.isPresent()){
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(new ApiResponse<>(HttpStatus.OK.value(), AppConstants.USER_DATA_FETCHED, userData.get()));
            }

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), AppConstants.USER_NOT_FOUND));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.SERVER_ERROR));
    }

    // Save user data
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

    // Map user dto to entity
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
