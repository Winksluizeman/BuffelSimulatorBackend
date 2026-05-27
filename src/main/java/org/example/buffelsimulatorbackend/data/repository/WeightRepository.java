package org.example.buffelsimulatorbackend.data.repository;

import org.example.buffelsimulatorbackend.application.port.IWeightRepository;
import org.example.buffelsimulatorbackend.data.converter.WeightConverter;
import org.example.buffelsimulatorbackend.data.table.WeightTable;
import org.example.buffelsimulatorbackend.domain.ExerciseModel;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class WeightRepository implements IWeightRepository {

    private final WeightJpaRepository weightJpaRepository;

    public WeightRepository(WeightJpaRepository weightJpaRepository) {
        this.weightJpaRepository = weightJpaRepository;
    }

    @Override
    public void save(ExerciseModel model) {
        WeightTable table = WeightConverter.toTable(model);
        weightJpaRepository.save(table);
    }

    @Override
    public List<ExerciseModel> findAllByUsername(String username) {
        return weightJpaRepository.findAllByUsername(username).stream()
                .map(WeightConverter::toModel)
                .toList();
    }
}