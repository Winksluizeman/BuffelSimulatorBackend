package org.example.buffelsimulatorbackend.domain;

import java.time.LocalDateTime;

public class UserModel {
    private Long id;
    private String username;
    private String email;
    private String password;
    private LocalDateTime createdAt;

    public UserModel(Long id, String username, String email, String password, LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}