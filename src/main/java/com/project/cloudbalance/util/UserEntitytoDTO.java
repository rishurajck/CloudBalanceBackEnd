package com.project.cloudbalance.util;

import com.project.cloudbalance.dto.user.UserRequestDTO;
import com.project.cloudbalance.entity.Role;
import com.project.cloudbalance.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserEntitytoDTO {

    public UserRequestDTO map(User entity)
    {
        UserRequestDTO dto = new UserRequestDTO();
        dto.setUsername(entity.getUsername());
        dto.setEmail(entity.getEmail());
        dto.setPassword(entity.getPassword());
        dto.setRole(entity.getRole().name());
        dto.setFirstname(entity.getFirstname());
        dto.setLastname(entity.getLastname());
        return dto;
    }
    public User map(UserRequestDTO dto)
    {
        User entity = new User();

        entity.setUsername(dto.getUsername());
        entity.setEmail(dto.getEmail());
        entity.setPassword(dto.getPassword());
        entity.setRole(Role.valueOf(dto.getRole().toUpperCase()));
        entity.setFirstname(dto.getFirstname());
        entity.setLastname(dto.getLastname());
        return entity;
    }
}
