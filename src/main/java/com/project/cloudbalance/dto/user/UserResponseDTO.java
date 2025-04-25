package com.project.cloudbalance.dto.user;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserResponseDTO {
    private Long id;
    private String username;
    private String firstname;
    private String lastname;
    private LocalDateTime lastLogin;
    private String email;
    private String role;

}
