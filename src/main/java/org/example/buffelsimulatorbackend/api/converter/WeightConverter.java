package org.example.buffelsimulatorbackend.api.converter;

import org.example.buffelsimulatorbackend.api.dto.WeightDto;
import org.example.buffelsimulatorbackend.domain.ExerciseModel;

public class WeightConverter {

    public static ExerciseModel toModel(WeightDto dto) {
        ExerciseModel model = new ExerciseModel();
        model.setName(dto.getName());
        model.setCategory(dto.getCategory());
        model.setWeight(dto.getWeight());
        model.setReps(dto.getReps());
        return model;
    }

    public static WeightDto toDto(ExerciseModel model) {
        WeightDto dto = new WeightDto();
        dto.setName(model.getName());
        dto.setCategory(model.getCategory());
        dto.setWeight(model.getWeight());
        dto.setReps(model.getReps());
        return dto;
    }
}