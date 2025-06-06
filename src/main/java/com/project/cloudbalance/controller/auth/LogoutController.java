package com.project.cloudbalance.controller.auth;

import com.project.cloudbalance.dto.api.ApiResponse;
import com.project.cloudbalance.service.blacklisttoken.BlacklistTokenService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.servlet.http.HttpServletRequest;
import net.snowflake.client.jdbc.internal.google.protobuf.Api;
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
    public ResponseEntity<ApiResponse> logout(HttpServletRequest request)
    {
        ApiResponse response = blacklistTokenService.blacklistToken(request);
        return ResponseEntity.ok().body(response);
    }
}


