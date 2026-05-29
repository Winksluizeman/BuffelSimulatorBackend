package org.example.buffelsimulatorbackend.domain;

public class ExerciseModel {
    private int id;
    private String username;
    private String name;
    private String category;
    private int weight;
    private int reps;

    public ExerciseModel() {}

    public ExerciseModel(int id, String username, String name, String category, int weight, int reps) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.category = category;
        this.weight = weight;
        this.reps = reps;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public int getWeight() { return weight; }
    public void setWeight(int weight) { this.weight = weight; }
    public int getReps() { return reps; }
    public void setReps(int reps) { this.reps = reps; }
}