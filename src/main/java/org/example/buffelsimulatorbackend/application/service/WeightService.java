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

    public List<ExerciseModel> getExercisesByUsername(String username) {
        return iWeightRepository.findAllByUsername(username);
    }

    public void saveExercise(ExerciseModel model) {
        if(model.getWeight() <0 ) {
            throw new IllegalArgumentException("Gewicht kan niet kleiner dan 0kg");
        }
        if (model.getCategory() == null || model.getCategory().isBlank()) {
            throw new IllegalArgumentException("Spiergroep veld is verplicht");
        }
        iWeightRepository.save(model);
        System.out.println("Opgeslagen: " + model.getName() + " - " + model.getCategory()
                + " - " + model.getWeight() + "kg - " + model.getReps() + " reps");
    }
}