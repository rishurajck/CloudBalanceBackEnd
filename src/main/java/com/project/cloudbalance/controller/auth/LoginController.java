package com.project.cloudbalance.controller.auth;

import com.project.cloudbalance.dto.login.LoginRequestDTO;
import com.project.cloudbalance.service.login.LoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class LoginController {

    private final LoginService loginService;
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequestDTO)
    {
        return loginService.login(loginRequestDTO);
    }

}
