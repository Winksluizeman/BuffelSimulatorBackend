package org.example.buffelsimulatorbackend.application.service;

import org.example.buffelsimulatorbackend.application.port.IWeightRepository;
import org.example.buffelsimulatorbackend.domain.ExerciseModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WeightService {

    private final IWeightRepository iWeightRepository;

    public WeightService(IWeightRepository iWeightRepository) {
        this.iWeightRepository = iWeightRepository;
    }

    public List<ExerciseModel> getExercises() {
        return iWeightRepository.findAll();
    }

    public void saveExercise(ExerciseModel model) {
        iWeightRepository.save(model);
        System.out.println("Opgeslagen: " + model.getName() + " - " + model.getCategory()
                + " - " + model.getWeight() + "kg - " + model.getReps() + " reps");
    }
}