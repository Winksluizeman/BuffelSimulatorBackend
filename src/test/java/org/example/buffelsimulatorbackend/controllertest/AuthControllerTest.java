package org.example.buffelsimulatorbackend.controllertest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.buffelsimulatorbackend.api.controller.AuthController;
import org.example.buffelsimulatorbackend.api.dto.LoginDto;
import org.example.buffelsimulatorbackend.api.dto.RegisterDto;
import org.example.buffelsimulatorbackend.application.service.AuthService;
import org.example.buffelsimulatorbackend.infrastructure.JwtFilter;
import org.example.buffelsimulatorbackend.infrastructure.JwtUtil;
import org.example.buffelsimulatorbackend.security.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@Import({SecurityConfig.class, JwtFilter.class})
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Test
    void register_withValidBody_shouldReturn200AndCallService() throws Exception {
        RegisterDto dto = new RegisterDto();
        dto.setUsername("Alice");
        dto.setEmail("alice@example.com");
        dto.setPassword("secret123");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        verify(authService).register("Alice", "alice@example.com", "secret123");
    }

    @Test
    void register_withMissingFields_shouldReturn400() throws Exception {
        RegisterDto dto = new RegisterDto();

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());

        verify(authService, never()).register(any(), any(), any());
    }

    @Test
    void login_withValidCredentials_shouldReturn200WithToken() throws Exception {
        LoginDto dto = new LoginDto();
        dto.setUsername("Alice");
        dto.setPassword("secret123");

        when(authService.login("Alice", "secret123")).thenReturn("mocked-jwt-token");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mocked-jwt-token"));

        verify(authService).login("Alice", "secret123");
    }

    @Test
    void login_withInvalidCredentials_shouldReturn401() throws Exception {
        LoginDto dto = new LoginDto();
        dto.setUsername("Alice");
        dto.setPassword("wrongpassword");

        when(authService.login("Alice", "wrongpassword"))
                .thenThrow(new RuntimeException("Wachtwoord onjuist"));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized());

        verify(authService).login("Alice", "wrongpassword");
    }
}