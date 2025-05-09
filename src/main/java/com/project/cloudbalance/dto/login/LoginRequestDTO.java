package com.project.cloudbalance.dto.login;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;


@Data
public class LoginRequestDTO {

    @NotBlank(message = "Username can't be empty")
    private String username;
    @NotBlank(message = "Password can't be empty")
//    @Pattern(
//            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$",
//            message = "Please Enter Correct Password"
//    )
//    Password must be at least 8 characters long and include uppercase, lowercase, number, and special character
    private String password;
}
