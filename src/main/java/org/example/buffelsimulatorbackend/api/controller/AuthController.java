// api/controller/AuthController.java
package org.example.buffelsimulatorbackend.api.controller;

import jakarta.validation.Valid;
import org.example.buffelsimulatorbackend.api.dto.LoginDto;
import org.example.buffelsimulatorbackend.api.dto.LoginResponseDto;
import org.example.buffelsimulatorbackend.api.dto.RegisterDto;
import org.example.buffelsimulatorbackend.application.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterDto registerDto) {
        authService.register(
                registerDto.getUsername(),
                registerDto.getEmail(),
                registerDto.getPassword()
        );
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginDto loginDto) {
        String token = authService.login(loginDto.getUsername(), loginDto.getPassword());
        return ResponseEntity.ok(new LoginResponseDto(token));
    }

    @ExceptionHandler (RuntimeException.class)
    public ResponseEntity<Void> handleRunTimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}