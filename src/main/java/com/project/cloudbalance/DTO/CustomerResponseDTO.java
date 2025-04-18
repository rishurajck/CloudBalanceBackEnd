package com.project.cloudbalance.DTO;

import com.project.cloudbalance.entity.Accounts;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CustomerResponseDTO {
    private Long id;
    private String username;
    private String firstname;
    private String lastname;
    private LocalDateTime lastLogin;
    private String email;
    private String role;
    private List<Accounts> accounts;
}
