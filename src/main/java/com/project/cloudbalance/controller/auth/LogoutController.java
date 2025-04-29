package com.project.cloudbalance.controller.auth;


import com.project.cloudbalance.service.blacklisttoken.BlacklistTokenService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class LogoutController {

    @Autowired
    private BlacklistTokenService blacklistTokenService;

    @PostMapping("/logout")
    @RolesAllowed({"ROLE_ADMIN", "ROLE_CUSTOMER", "ROLE_READ_ONLY"})
    public ResponseEntity<String> logout(HttpServletRequest request)
    {
        return blacklistTokenService.blacklistToken(request);
    }
}


