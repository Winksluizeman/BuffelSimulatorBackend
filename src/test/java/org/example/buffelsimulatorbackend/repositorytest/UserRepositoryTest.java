package org.example.buffelsimulatorbackend.repositorytest;

import org.example.buffelsimulatorbackend.data.repository.UserJpaRepository;
import org.example.buffelsimulatorbackend.data.repository.UserRepository;
import org.example.buffelsimulatorbackend.domain.UserModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(UserRepository.class)
class UserRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15");

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Test
    void save_withValidUser_shouldPersistAllFields() {
        UserModel user = new UserModel(
                null,
                "Alice",
                "alice@example.com",
                "$2a$10$hashedpassword123456789012345678901234567890123456789012",
                LocalDateTime.now()
        );

        userRepository.save(user);

        Optional<UserModel> result = userRepository.findByUsername("Alice");
        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo("Alice");
        assertThat(result.get().getEmail()).isEqualTo("alice@example.com");
        assertThat(result.get().getPassword()).isEqualTo("$2a$10$hashedpassword123456789012345678901234567890123456789012");
    }

    @Test
    void findByUsername_withExistingUsername_shouldReturnUser() {
        UserModel user = new UserModel(
                null,
                "Bob",
                "bob@example.com",
                "$2a$10$hashedpassword123456789012345678901234567890123456789012",
                LocalDateTime.now()
        );
        userRepository.save(user);

        Optional<UserModel> result = userRepository.findByUsername("Bob");

        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo("Bob");
    }

    @Test
    void findByUsername_withUnknownUsername_shouldReturnEmpty() {
        Optional<UserModel> result = userRepository.findByUsername("onbekend");

        assertThat(result).isEmpty();
    }

    @Test
    void save_withDuplicateEmail_shouldThrowException() {
        UserModel user1 = new UserModel(
                null,
                "Alice",
                "duplicate@example.com",
                "$2a$10$hashedpassword123456789012345678901234567890123456789012",
                LocalDateTime.now()
        );
        UserModel user2 = new UserModel(
                null,
                "Bob",
                "duplicate@example.com",
                "$2a$10$hashedpassword123456789012345678901234567890123456789012",
                LocalDateTime.now()
        );

        userRepository.save(user1);

        assertThatThrownBy(() -> {
            userRepository.save(user2);
            userJpaRepository.flush();
        }).isInstanceOf(DataIntegrityViolationException.class);
    }
}