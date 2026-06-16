package org.example.buffelsimulatorbackend.api.controller;

import jakarta.validation.Valid;
import org.example.buffelsimulatorbackend.api.converter.WeightConverter;
import org.example.buffelsimulatorbackend.api.dto.WeightDto;
import org.example.buffelsimulatorbackend.application.service.WeightService;
import org.example.buffelsimulatorbackend.domain.ExerciseModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("/Weight")
public class WeightController {

    private final WeightService weightService;
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public WeightController(WeightService weightService) {
        this.weightService = weightService;
    }

    @GetMapping("/events")
    public SseEmitter streamEvents(Authentication authentication) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.add(emitter);
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        return emitter;
    }

    @PostMapping
    public ResponseEntity<Void> saveExercise(@Valid @RequestBody WeightDto weightDto,
                                             Authentication authentication) {
        ExerciseModel model = WeightConverter.toModel(weightDto);
        model.setUsername(authentication.getName());
        weightService.saveExercise(model);
        sendEvent("exercise-saved");
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<WeightDto>> getExercises(Authentication authentication) {
        String username = authentication.getName();
        List<ExerciseModel> models = weightService.getExercisesByUsername(username);
        List<WeightDto> dtos = models.stream()
                .map(WeightConverter::toDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    private void sendEvent(String eventName) {
        List<SseEmitter> deadEmitters = new ArrayList<>();
        emitters.forEach(emitter -> {
            try {
                emitter.send(SseEmitter.event().name(eventName).data("update"));
            } catch (IOException e) {
                deadEmitters.add(emitter);
            }
        });
        emitters.removeAll(deadEmitters);
    }
}