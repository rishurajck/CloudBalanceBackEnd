package com.project.cloudbalance.controller.auth;

import com.project.cloudbalance.dto.login.LoginRequestDTO;
import com.project.cloudbalance.dto.login.LoginResponseDTO;
import com.project.cloudbalance.service.login.LoginService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class LoginController {


    private LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO loginRequestDTO)
    {
        LoginResponseDTO response = loginService.login(loginRequestDTO);
        return ResponseEntity.ok().body(response);
    }

}
