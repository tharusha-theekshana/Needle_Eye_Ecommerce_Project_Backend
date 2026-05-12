package com.needleeye.auth_service.Service.Impl;

import com.needleeye.auth_service.Configuration.OpenFeign.UserServiceClient;
import com.needleeye.auth_service.Dto.Request.LoginRequestDto;
import com.needleeye.auth_service.Dto.Request.RegisterRequestDto;
import com.needleeye.auth_service.Dto.Request.UserRegisterEventDto;
import com.needleeye.auth_service.Dto.Response.ApiResponse;
import com.needleeye.auth_service.Dto.Response.LoginResponseDto;
import com.needleeye.auth_service.Entity.AuthUser;
import com.needleeye.auth_service.Repository.AuthRepo;
import com.needleeye.auth_service.Service.AuthService;
import com.needleeye.auth_service.Service.KafkaProducerService;
import com.needleeye.auth_service.Utils.Constants.AppConstants;
import com.needleeye.auth_service.Utils.Jwt.JwtUtils;
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
    private KafkaProducerService kafkaProducerService;
    private JwtUtils jwtUtil;

    public AuthServiceImpl(AuthRepo authRepo, PasswordEncoder passwordEncoder, UserServiceClient userServiceClient, KafkaProducerService kafkaProducerService, JwtUtils jwtUtil) {
        this.authRepo = authRepo;
        this.passwordEncoder = passwordEncoder;
        this.userServiceClient = userServiceClient;
        this.kafkaProducerService = kafkaProducerService;
        this.jwtUtil = jwtUtil;
    }

    // User registration
    @Override
    public ResponseEntity<ApiResponse<?>> registerUser(RegisterRequestDto registerRequestData) {
        try {
            Optional<AuthUser> existUser = authRepo.findByEmail(registerRequestData.getEmail());

            if (existUser.isPresent()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), AppConstants.EMAIL_EXISTS));
            }

            AuthUser mappedUser = mapDtoToAuthUserEntity(registerRequestData);
            authRepo.save(mappedUser);

            // Send register data to user service
            registerRequestData.setUserId(mappedUser.getUserId());
            ResponseEntity<ApiResponse<?>> response = userServiceClient.saveUserData(registerRequestData);

            if (response.getBody().getCode() == 200) {
                UserRegisterEventDto eventData = new UserRegisterEventDto(
                        mappedUser.getUserId(),
                        mappedUser.getEmail(),
                        registerRequestData.getFirstName() + " " + registerRequestData.getLastName()
                );

                kafkaProducerService.sendUserRegisteredEvent(eventData);

                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(new ApiResponse<>(HttpStatus.OK.value(), AppConstants.USER_REG_SUCCESS));
            }
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.USER_REG_FAIL));


        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.SERVER_ERROR));
    }

    // User login
    @Override
    public ResponseEntity<ApiResponse<?>> login(LoginRequestDto loginRequestData) {
        try{
            Optional<AuthUser> user = authRepo.findByEmail(loginRequestData.getEmail());

            if(user.isEmpty() || !passwordEncoder.matches(loginRequestData.getPassword(), user.get().getPassword())){
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), AppConstants.INVALID_CREDENTIALS));
            }

            user.get().setLoginAttempts(user.get().getLoginAttempts() + 1);
            authRepo.save(user.get());

            String token = jwtUtil.generateToken(
                    user.get().getUserId(),
                    user.get().getEmail(),
                    user.get().getRole().toString()
            );

            LoginResponseDto loginResponse = new LoginResponseDto();
            loginResponse.setUserId(user.get().getUserId());
            loginResponse.setLoginAttempts(user.get().getLoginAttempts());
            loginResponse.setToken(token);
            loginResponse.setRole(user.get().getRole().toString());

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ApiResponse<>(HttpStatus.OK.value(), AppConstants.USER_LOGIN_SUCCESS,loginResponse));


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

    // Generate custom user Id
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
