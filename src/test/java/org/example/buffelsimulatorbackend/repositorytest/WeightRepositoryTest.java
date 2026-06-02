package org.example.buffelsimulatorbackend.repositorytest;

import org.example.buffelsimulatorbackend.data.repository.WeightRepository;
import org.example.buffelsimulatorbackend.data.repository.WeightJpaRepository;
import org.example.buffelsimulatorbackend.domain.ExerciseModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(WeightRepository.class)
class WeightRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15");

    @Autowired
    private WeightRepository weightRepository;

    @Autowired
    private WeightJpaRepository weightJpaRepository;

    @Test
    void save_withValidExercise_shouldPersistAllFields() {
        ExerciseModel model = new ExerciseModel(0, "Alice", "Bench Press", "Push", 60, 8);

        weightRepository.save(model);

        List<ExerciseModel> results = weightRepository.findAllByUsername("Alice");
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getUsername()).isEqualTo("Alice");
        assertThat(results.get(0).getName()).isEqualTo("Bench Press");
        assertThat(results.get(0).getCategory()).isEqualTo("Push");
        assertThat(results.get(0).getWeight()).isEqualTo(60);
        assertThat(results.get(0).getReps()).isEqualTo(8);
    }

    @Test
    void findAllByUsername_shouldReturnOnlyRecordsOfGivenUser() {
        weightRepository.save(new ExerciseModel(0, "Alice", "Bench Press", "Push", 60, 8));
        weightRepository.save(new ExerciseModel(0, "Alice", "Squat", "Legs", 100, 5));
        weightRepository.save(new ExerciseModel(0, "Bob", "Deadlift", "Pull", 120, 3));

        List<ExerciseModel> aliceResults = weightRepository.findAllByUsername("Alice");
        List<ExerciseModel> bobResults = weightRepository.findAllByUsername("Bob");

        assertThat(aliceResults).hasSize(2);
        assertThat(aliceResults).allMatch(e -> e.getUsername().equals("Alice"));

        assertThat(bobResults).hasSize(1);
        assertThat(bobResults).allMatch(e -> e.getUsername().equals("Bob"));
    }

    @Test
    void findAllByUsername_shouldNotLeakRecordsOfOtherUsers() {
        weightRepository.save(new ExerciseModel(0, "Alice", "Bench Press", "Push", 60, 8));
        weightRepository.save(new ExerciseModel(0, "Bob", "Deadlift", "Pull", 120, 3));

        List<ExerciseModel> aliceResults = weightRepository.findAllByUsername("Alice");

        assertThat(aliceResults).noneMatch(e -> e.getUsername().equals("Bob"));
    }

    @Test
    void save_withMissingName_shouldThrowException() {
        ExerciseModel model = new ExerciseModel(0, "Alice", null, "Push", 60, 8);

        assertThatThrownBy(() -> {
            weightRepository.save(model);
            weightJpaRepository.flush(); // forceert de INSERT naar de database
        }).isInstanceOf(Exception.class);
    }
}