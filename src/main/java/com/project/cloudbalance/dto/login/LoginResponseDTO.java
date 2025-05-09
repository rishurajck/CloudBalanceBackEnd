package com.project.cloudbalance.dto.login;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO {
    private String firstname;
    private String username;
    private String role;
    private String token;

}
