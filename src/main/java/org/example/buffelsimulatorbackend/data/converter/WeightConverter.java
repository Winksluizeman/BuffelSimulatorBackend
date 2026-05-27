package org.example.buffelsimulatorbackend.data.converter;

import org.example.buffelsimulatorbackend.data.table.WeightTable;
import org.example.buffelsimulatorbackend.domain.ExerciseModel;

public class WeightConverter {

    public static WeightTable toTable(ExerciseModel model) {
        WeightTable table = new WeightTable();
        table.setUsername(model.getUsername());
        table.setName(model.getName());
        table.setCategory(model.getCategory());
        table.setWeight(model.getWeight());
        table.setReps(model.getReps());
        return table;
    }

    public static ExerciseModel toModel(WeightTable table) {
        ExerciseModel model = new ExerciseModel();
        model.setUsername(table.getUsername());
        model.setName(table.getName());
        model.setCategory(table.getCategory());
        model.setWeight(table.getWeight());
        model.setReps(table.getReps());
        return model;
    }
}