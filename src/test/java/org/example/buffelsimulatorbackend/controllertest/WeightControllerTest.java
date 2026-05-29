package org.example.buffelsimulatorbackend.controllertest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.buffelsimulatorbackend.api.controller.WeightController;
import org.example.buffelsimulatorbackend.api.dto.WeightDto;
import org.example.buffelsimulatorbackend.application.service.WeightService;
import org.example.buffelsimulatorbackend.domain.ExerciseModel;
import org.example.buffelsimulatorbackend.infrastructure.JwtFilter;
import org.example.buffelsimulatorbackend.infrastructure.JwtUtil;
import org.example.buffelsimulatorbackend.security.SecurityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@WebMvcTest(WeightController.class)
@Import({SecurityConfig.class, JwtFilter.class})
class WeightControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private WeightService weightService;

    @MockitoBean
    private JwtUtil jwtUtil;

    private static final String VALID_TOKEN = "Bearer valid-token";

    @BeforeEach
    void setUp() {
        when(jwtUtil.isTokenValid("valid-token")).thenReturn(true);
        when(jwtUtil.extractUsername("valid-token")).thenReturn("Alice");
    }

    @Test
    void saveExercise_withValidRequestAndToken_shouldReturn201() throws Exception {
        WeightDto dto = new WeightDto();
        dto.setName("Bench Press");
        dto.setCategory("Push");
        dto.setWeight(60);
        dto.setReps(8);

        mockMvc.perform(post("/Weight")
                        .header("Authorization", VALID_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        ArgumentCaptor<ExerciseModel> captor = ArgumentCaptor.forClass(ExerciseModel.class);
        verify(weightService).saveExercise(captor.capture());
        assertThat(captor.getValue().getUsername()).isEqualTo("Alice");
        assertThat(captor.getValue().getName()).isEqualTo("Bench Press");
    }

    @Test
    void saveExercise_withoutToken_shouldReturn401() throws Exception {
        WeightDto dto = new WeightDto();
        dto.setName("Bench Press");
        dto.setCategory("Push");
        dto.setWeight(60);
        dto.setReps(8);

        mockMvc.perform(post("/Weight")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized());

        verify(weightService, never()).saveExercise(any());
    }

    @Test
    void saveExercise_withMissingName_shouldReturn400() throws Exception {
        WeightDto dto = new WeightDto();
        dto.setCategory("Push");
        dto.setWeight(60);
        dto.setReps(8);

        mockMvc.perform(post("/Weight")
                        .header("Authorization", VALID_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());

        verify(weightService, never()).saveExercise(any());
    }

    @Test
    void getExercises_withValidToken_shouldReturn200WithExercisesOfCurrentUser() throws Exception {
        ExerciseModel model = new ExerciseModel(1, "Alice", "Bench Press", "Push", 60, 8);
        when(weightService.getExercisesByUsername("Alice")).thenReturn(List.of(model));

        mockMvc.perform(get("/Weight")
                        .header("Authorization", VALID_TOKEN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Bench Press"))
                .andExpect(jsonPath("$[0].category").value("Push"))
                .andExpect(jsonPath("$[0].weight").value(60))
                .andExpect(jsonPath("$[0].reps").value(8));

        verify(weightService).getExercisesByUsername("Alice");
    }

    @Test
    void getExercises_withoutToken_shouldReturn401() throws Exception {
        mockMvc.perform(get("/Weight"))
                .andExpect(status().isUnauthorized());

        verify(weightService, never()).getExercisesByUsername(any());
    }
}