package com.jwt.demo.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignupRequestDto {

    @Size(min = 8, max = 50, message = "Username must be 8 to 50 characters.")
    private String username;

    // Matches the explicit requirement: must contain @ and _
    @Pattern(regexp = "^(?=.*@)(?=.*_).+$", message = "Email must contain @ and _.")
    private String email;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,12}$",
            message = "Password must be 8-12 characters, with 1 uppercase, 1 lowercase, 1 number, and 1 special character.")
    private String password;

    @Pattern(regexp = "^(Standard User|Administrator|Admin)$", message = "User Type must be 'Standard User' or 'Administrator'.")
    private String userType;
}
