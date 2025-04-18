package com.project.cloudbalance.DTO;

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
