package com.example.webserver.controller;

import com.example.webserver.dto.*;
import com.example.webserver.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @Valid @RequestBody RegisterRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authService.register(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(
            @RequestBody RefreshTokenRequest request) {

        return ResponseEntity.ok(
                authService.refreshToken(request)
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            @RequestBody LogoutRequest request) {

        authService.logout(request);

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "Logged out successfully"
                )
        );
    }
}
