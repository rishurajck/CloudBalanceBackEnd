package com.project.cloudbalance.service.login;

import com.project.cloudbalance.dto.login.LoginRequestDTO;
import com.project.cloudbalance.dto.login.LoginResponseDTO;
import com.project.cloudbalance.entity.User;
import com.project.cloudbalance.exception.customException.InvalidCredentialsException;
import com.project.cloudbalance.repository.UserRepository;
import com.project.cloudbalance.security.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
public class LoginService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthService authService;

    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {

        if (loginRequestDTO.getUsername().isEmpty() || loginRequestDTO.getPassword().isEmpty()) {
            throw new InvalidCredentialsException("Username or password cannot be null");
        }
        Optional<User> userOpt = userRepository.findByUsername(loginRequestDTO.getUsername());

        if (userOpt.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        try {
            String token = authService.authenticateAndGenerateToken(
                    loginRequestDTO.getUsername(),
                    loginRequestDTO.getPassword(),
                    userOpt.get().getRole().name()
            );
            User user = userOpt.get();
            user.setLastLogin(LocalDateTime.now());
            userRepository.save(user);
            LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
            loginResponseDTO.setToken(token);
            loginResponseDTO.setFirstname(userOpt.get().getFirstname());
            loginResponseDTO.setUsername(userOpt.get().getUsername());
            loginResponseDTO.setRole(userOpt.get().getRole().name());
            log.info("Login successful");
            return loginResponseDTO;

        } catch (Exception e) {

            throw new InvalidCredentialsException("Invalid username or password");
        }
    }

}