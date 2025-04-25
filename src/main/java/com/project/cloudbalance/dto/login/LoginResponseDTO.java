package com.project.cloudbalance.dto.login;

import lombok.Data;

@Data
public class LoginResponseDTO {
    private String firstname;
    private String username;
    private String role;
    private String token;
}
