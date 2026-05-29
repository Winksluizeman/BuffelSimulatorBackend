package org.example.buffelsimulatorbackend.servicetests;

import org.example.buffelsimulatorbackend.application.port.IUserRepository;
import org.example.buffelsimulatorbackend.application.service.AuthService;
import org.example.buffelsimulatorbackend.domain.UserModel;
import org.example.buffelsimulatorbackend.infrastructure.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private IUserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @Test
    void register_shouldSaveUserWithHashedPassword() {
        // Arrange
        when(passwordEncoder.encode("plaintext123")).thenReturn("hashed123");

        // Act
        authService.register("Alice", "alice@example.com", "plaintext123");

        // Assert
        ArgumentCaptor<UserModel> captor = ArgumentCaptor.forClass(UserModel.class);
        verify(userRepository).save(captor.capture());

        UserModel saved = captor.getValue();
        assertThat(saved.getUsername()).isEqualTo("Alice");
        assertThat(saved.getEmail()).isEqualTo("alice@example.com");
        assertThat(saved.getPassword()).isEqualTo("hashed123");
        assertThat(saved.getPassword()).isNotEqualTo("plaintext123");
    }

    @Test
    void login_shouldReturnTokenOnValidCredentials() {
        // Arrange
        UserModel user = new UserModel(1L, "Alice", "alice@example.com", "hashed123", LocalDateTime.now());

        when(userRepository.findByUsername("Alice")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("plaintext123", "hashed123")).thenReturn(true);
        when(jwtUtil.generateToken("Alice")).thenReturn("jwt-token-abc");

        // Act
        String token = authService.login("Alice", "plaintext123");

        // Assert
        assertThat(token).isEqualTo("jwt-token-abc");
        verify(jwtUtil).generateToken("Alice");
    }

    @Test
    void login_shouldThrowExceptionWhenUserNotFound() {
        // Arrange
        when(userRepository.findByUsername("OnbekendUser")).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> authService.login("OnbekendUser", "wachtwoord"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Gebruiker niet gevonden");

        verify(jwtUtil, never()).generateToken(any());
    }

    @Test
    void login_shouldThrowExceptionWhenPasswordIncorrect() {
        // Arrange
        UserModel user = new UserModel(1L, "Alice", "alice@example.com", "hashed123", LocalDateTime.now());

        when(userRepository.findByUsername("Alice")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("verkeerd", "hashed123")).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> authService.login("Alice", "verkeerd"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Wachtwoord onjuist");

        verify(jwtUtil, never()).generateToken(any());
    }
}