package com.needleeye.auth_service.Dto.Request;

import com.needleeye.auth_service.Utils.Enums.UserRoles;
import jakarta.validation.constraints.*;

public class RegisterRequestDto {

    private String userId;

    @NotBlank(message = "First name is required.")
    @Size(max = 50, message = "First name must not exceed 50 characters.")
    @Pattern(regexp = "^[A-Za-z]+$", message = "First name must contain letters only.")
    private String firstName;

    @NotBlank(message = "Last name is required.")
    @Size(max = 50, message = "Last name must not exceed 50 characters.")
    @Pattern(regexp = "^[A-Za-z]+(?:\\s[A-Za-z]+)*$", message = "Last name must contain letters only.")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Size(max = 75, message = "Email must not exceed 75 characters.")
    @Pattern(
            regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$",
            message = "Email is not valid."
    )
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*\\d).+$",
            message = "Password must contain at least one uppercase letter and one number"
    )
    private String password;

    @NotBlank(message = "Mobile number is required")
    @Size(max = 10, message = "Mobile number length is not valid.")
    @Pattern(
            regexp = "^07[0-9]{8}$",
            message = "Mobile number must be in format 07XXXXXXXX"
    )
    private String mobileNumber;

    @NotNull(message = "User role is required")
    private UserRoles userRole;

    public RegisterRequestDto() {
    }

    public RegisterRequestDto(String userId, String firstName, String lastName, String email, String password, String mobileNumber, @NotNull(message = "User role is required") UserRoles userRole) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.mobileNumber = mobileNumber;
        this.userRole = userRole;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public UserRoles getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRoles userRole) {
        this.userRole = userRole;
    }
}
