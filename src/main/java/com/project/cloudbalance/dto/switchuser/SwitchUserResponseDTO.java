package com.project.cloudbalance.dto.switchuser;

import lombok.Data;

@Data
public class SwitchUserResponseDTO {
    private String firstname;
    private String role;
    private String token;
    private String username;
}
