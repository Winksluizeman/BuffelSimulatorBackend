package org.example.buffelsimulatorbackend.application.service;

import org.example.buffelsimulatorbackend.application.port.IUserRepository;
import org.example.buffelsimulatorbackend.domain.UserModel;
import org.example.buffelsimulatorbackend.infrastructure.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {

    private final IUserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthService(IUserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(String name, String email, String password)
    {
        UserModel user = new UserModel(
                null,
                name,
                email,
                passwordEncoder.encode(password),
                LocalDateTime.now()
        );
        userRepository.save(user);
    }

    public String login(String username, String password) {
        UserModel user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Gebruiker niet gevonden"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Wachtwoord onjuist");
        }

        return jwtUtil.generateToken(username);
    }
}