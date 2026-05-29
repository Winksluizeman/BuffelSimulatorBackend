package org.example.buffelsimulatorbackend.servicetests;

import org.example.buffelsimulatorbackend.application.port.IWeightRepository;
import org.example.buffelsimulatorbackend.application.service.WeightService;
import org.example.buffelsimulatorbackend.domain.ExerciseModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WeightServiceTest {

    @Mock
    private IWeightRepository weightRepository;

    @InjectMocks
    private WeightService weightService;

    @Test
    void getExercisesByUsername_shouldCallRepositoryAndReturnResults() {
        // Arrange
        String username = "Alice";

        ExerciseModel model = new ExerciseModel(
                1,
                "Alice",
                "Bench Press",
                "Push",
                60,
                8
        );

        List<ExerciseModel> expected = List.of(model);

        when(weightRepository.findAllByUsername(username)).thenReturn(expected);

        // Act
        List<ExerciseModel> result = weightService.getExercisesByUsername(username);

        // Assert
        verify(weightRepository).findAllByUsername(username);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void saveExercise_shouldCallRepositoryWithCorrectModel() {
        // Arrange
        ExerciseModel model = new ExerciseModel(
                1,
                "Alice",
                "Bench Press",
                "Push",
                60,
                8
        );

        // Act
        weightService.saveExercise(model);

        // Assert
        ArgumentCaptor<ExerciseModel> captor = ArgumentCaptor.forClass(ExerciseModel.class);
        verify(weightRepository).save(captor.capture());

        ExerciseModel saved = captor.getValue();

        assertThat(saved.getId()).isEqualTo(1);
        assertThat(saved.getUsername()).isEqualTo("Alice");
        assertThat(saved.getName()).isEqualTo("Bench Press");
        assertThat(saved.getCategory()).isEqualTo("Push");
        assertThat(saved.getWeight()).isEqualTo(60);
        assertThat(saved.getReps()).isEqualTo(8);
    }

    @Test
    void getExercisesByUsername_shouldNotReturnExercisesFromOtherUser() {
        ExerciseModel aliceModel = new ExerciseModel(1, "Alice", "Bench Press", "Push", 60, 8);
        ExerciseModel bobModel = new ExerciseModel(2, "Bob", "Squat", "Legs", 100, 5);

        when(weightRepository.findAllByUsername("Alice")).thenReturn(List.of(aliceModel));

        List<ExerciseModel> result = weightService.getExercisesByUsername("Alice");

        assertThat(result).doesNotContain(bobModel);
        assertThat(result).extracting(ExerciseModel::getUsername).containsOnly("Alice");
    }

    @Test
    void saveExercise_withNegativeWeight_shouldThrowException() {
        ExerciseModel model = new ExerciseModel(1, "Alice", "Bench Press", "Push", -10, 8);

        assertThatThrownBy(() -> weightService.saveExercise(model))
                .isInstanceOf(IllegalArgumentException.class);

        verify(weightRepository, never()).save(any());
    }
}
