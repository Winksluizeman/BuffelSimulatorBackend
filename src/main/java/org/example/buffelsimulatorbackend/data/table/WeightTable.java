package org.example.buffelsimulatorbackend.data.table;

import jakarta.persistence.*;

@Entity
@Table(name = "exercise")
public class WeightTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String category;
    private int weight;
    private int reps;

    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public int getWeight() { return weight; }
    public void setWeight(int weight) { this.weight = weight; }
    public int getReps() { return reps; }
    public void setReps(int reps) { this.reps = reps; }
}