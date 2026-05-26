package org.example.buffelsimulatorbackend.application.port;

import org.example.buffelsimulatorbackend.domain.ExerciseModel;
import java.util.List;

public interface IWeightRepository {
    void save(ExerciseModel model);
    List<ExerciseModel> findAll();
}
