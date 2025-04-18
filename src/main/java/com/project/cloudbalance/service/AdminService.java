package com.project.cloudbalance.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("hasRole('ADMIN')")
public class AdminService {
    public ResponseEntity<String> admin() {
        return ResponseEntity.ok("Admin access granted");
    }
}
