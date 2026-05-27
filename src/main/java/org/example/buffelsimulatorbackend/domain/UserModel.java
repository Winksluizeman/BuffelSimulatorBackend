package org.example.buffelsimulatorbackend.domain;

import java.time.LocalDateTime;

public class UserModel {
    private Long id;
    private String name;
    private String email;
    private String password;
    private LocalDateTime createdAt;

    public UserModel(Long id, String name, String email, String password, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}