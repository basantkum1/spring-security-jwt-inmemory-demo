package com.jwt.demo.controller;

import com.jwt.demo.dto.SignupRequestDto;
import com.jwt.demo.entity.User;
import com.jwt.demo.repository.UserInMemRepository;
import com.jwt.demo.security.JwtUtils;
import com.jwt.demo.service.CountryLocationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class UserAuthController {

    private final UserInMemRepository userInMemRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final CountryLocationService locationService;

    //Injecting all above declared beans via constructor injection
    @Autowired
    public UserAuthController(UserInMemRepository repo, PasswordEncoder encoder, JwtUtils jwt, CountryLocationService locationService) {
        this.userInMemRepository = repo;
        this.passwordEncoder = encoder;
        this.jwtUtils = jwt;
        this.locationService = locationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@Valid @RequestBody SignupRequestDto requestDto, HttpServletRequest httpRequest) {
        if (locationService.isRestricted(httpRequest.getRemoteAddr())) {
            return ResponseEntity.status(403).body("Registration blocked: Unauthorized country.");
        }
        if (userInMemRepository.findByUsername(requestDto.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username exists");
        }

        User user = new User();
        user.setUsername(requestDto.getUsername());
        user.setEmail(requestDto.getEmail());
        user.setPassword(passwordEncoder.encode(requestDto.getPassword())); // Encoded password
        user.setUserType(requestDto.getUserType());
        userInMemRepository.save(user);

        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password) {

        User user = userInMemRepository.findByUsername(username).orElse(null);

        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            // Include role in token to allow @PreAuthorize to work
            log.info("inside login check***************************************");
            String token = jwtUtils.generateToken(username, user.getUserType());
            return ResponseEntity.ok(token);
        }
        // Redirects to error endpoint on invalid login
        return ResponseEntity.status(401).header("Location", "/api/auth/error").build();
    }

    @GetMapping("/error")
    public ResponseEntity<String> loginError() {
        return ResponseEntity.status(401).body("Login Failed"); // Exact message required
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String header, @RequestParam String username) {
        if (header != null && header.startsWith("Bearer ")) {
            jwtUtils.logoutToken(header.substring(7)); // Invalidates session
        }
        return ResponseEntity.ok(username + " Logged Out"); // Exact message required
    }
}
