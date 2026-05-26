package org.example.buffelsimulatorbackend.api.controller;

import org.example.buffelsimulatorbackend.api.converter.WeightConverter;
import org.example.buffelsimulatorbackend.api.dto.WeightDto;
import org.example.buffelsimulatorbackend.application.service.WeightService;
import org.example.buffelsimulatorbackend.domain.ExerciseModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Weight")
public class WeightController {

    private final WeightService weightService;

    public WeightController(WeightService weightService) {
        this.weightService = weightService;
    }

    @PostMapping
    public ResponseEntity<Void> saveExercise(@RequestBody WeightDto weightDto) {
        ExerciseModel model = WeightConverter.toModel(weightDto);
        weightService.saveExercise(model);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<WeightDto>> getExercises() {
        List<ExerciseModel> models = weightService.getExercises();
        List<WeightDto> dtos = models.stream()
                .map(WeightConverter::toDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }
}