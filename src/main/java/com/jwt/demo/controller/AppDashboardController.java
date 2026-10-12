package com.jwt.demo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AppDashboardController {

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication) {
        // Automatically extracts the username from the verified JWT
        return "Welcome " + authentication.getName();
    }

    @GetMapping("/settings")
    @PreAuthorize("hasAuthority('Administrator')") // Requirement: Only Admin access
    public String settings() {
        return "Settings Access Granted";
    }
}
