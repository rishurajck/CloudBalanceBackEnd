package com.project.cloudbalance.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LoginResponseDTO {
    private String firstname;
    private String role;
    private String token;
}
