package com.needleeye.auth_service.Service.Impl;

import com.needleeye.auth_service.Configuration.OpenFeign.UserServiceClient;
import com.needleeye.auth_service.Dto.Request.RegisterRequestDto;
import com.needleeye.auth_service.Dto.Response.ApiResponse;
import com.needleeye.auth_service.Entity.AuthUser;
import com.needleeye.auth_service.Repository.AuthRepo;
import com.needleeye.auth_service.Service.AuthService;
import com.needleeye.auth_service.Utils.Constants.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private AuthRepo authRepo;
    private PasswordEncoder passwordEncoder;
    private UserServiceClient userServiceClient;

    public AuthServiceImpl(AuthRepo authRepo, PasswordEncoder passwordEncoder, UserServiceClient userServiceClient) {
        this.authRepo = authRepo;
        this.passwordEncoder = passwordEncoder;
        this.userServiceClient = userServiceClient;
    }

    @Override
    public ResponseEntity<ApiResponse<?>> registerUser(RegisterRequestDto registerRequestData) {
        try{
            Optional<AuthUser> existUser = authRepo.findByEmail(registerRequestData.getEmail());

            if(existUser.isPresent()){
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), AppConstants.EMAIL_EXISTS));
            }

            AuthUser mappedUser = mapDtoToAuthUserEntity(registerRequestData);
            authRepo.save(mappedUser);

            ResponseEntity<ApiResponse<?>> response = userServiceClient.saveUserData(registerRequestData);
            System.out.println(response);




        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.SERVER_ERROR));
    }

   AuthUser mapDtoToAuthUserEntity(RegisterRequestDto requestData) {
        AuthUser user = new AuthUser();

        user.setUserId(generateUserId());
        user.setEmail(requestData.getEmail());
        user.setPassword(passwordEncoder.encode(requestData.getPassword()));
        user.setRole(requestData.getUserRole());

       LocalDateTime now = LocalDateTime.now();

       user.setCreatedAt(now);
       user.setUpdatedAt(now);

       user.setActive(true);
       user.setLoginAttempts(0);

        return user;
    }

    public String generateUserId() {
        Optional<AuthUser> lastUser = authRepo.findTopByOrderByIdDesc();

        int nextNumber = 1;

        if (lastUser.isPresent() && lastUser.get().getUserId() != null) {
            String lastId = lastUser.get().getUserId();
            String numberPart = lastId.substring(4);
            nextNumber = Integer.parseInt(numberPart) + 1;
        }

        return String.format("NEYE%06d", nextNumber);
    }

}
