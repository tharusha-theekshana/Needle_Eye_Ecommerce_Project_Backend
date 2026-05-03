package com.needleeye.auth_service.Dto.Request;

import com.needleeye.auth_service.Utils.Enums.UserRoles;
import jakarta.validation.constraints.*;

public class RegisterRequestDto {

    @NotBlank(message = "First name is required.")
    @Size(max = 50, message = "First name must not exceed 50 characters.")
    @Pattern(regexp = "^[A-Za-z]+$", message = "First name must contain letters only.")
    private String firstName;

    @NotBlank(message = "Last name is required.")
    @Size(max = 50, message = "Last name must not exceed 50 characters.")
    @Pattern(regexp = "^[A-Za-z]+$", message = "Last name must contain letters only.")
    private String lastName;

    @NotNull(message = "Age is required.")
    @Min(value = 10, message = "Age must be at least 10.")
    @Max(value = 99, message = "Age must not exceed 99.")
    private Integer age;

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


    @NotBlank(message = "Address is required.")
    @Size(max = 200, message = "Address must not exceed 75 characters.")
    private String address;

    @NotNull(message = "User role is required")
    private UserRoles userRole;

    public RegisterRequestDto() {
    }

    public RegisterRequestDto(String firstName, String lastName, @NotNull(message = "Age is required.") Integer age, String email, String password, String mobileNumber, String address, @NotNull(message = "User role is required") UserRoles userRole) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.email = email;
        this.password = password;
        this.mobileNumber = mobileNumber;
        this.address = address;
        this.userRole = userRole;
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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public UserRoles getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRoles userRole) {
        this.userRole = userRole;
    }
}
