package com.project.cloudbalance.dto.user;

import lombok.Data;
import java.util.List;

@Data
public class UserRequestDTO {
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String role;
    private List<Long> accounts;
}
