package com.project.cloudbalance.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.List;

@Data
public class UserRequestDTO {
    @NotBlank(message = "Username can't be empty")
    private String username;

    @NotBlank(message = "FirstName shouldn't be empty")
    private String firstname;

    private String lastname;

    @NotBlank(message = "Email shouldn't be empty")
    @Pattern(
            regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$",
            message = "Invalid email format"
    )
    private String email;

    @NotBlank(message = "Password shouldn't be empty")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$",
            message = "Please Enter Correct Password"
    )
    private String password;

    @NotBlank(message = "Please Select Roles")
    private String role;

    private List<Long> accounts;
}
