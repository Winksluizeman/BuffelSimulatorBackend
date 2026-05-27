package org.example.buffelsimulatorbackend.data.table;

import jakarta.persistence.*;

@Entity
@Table(name = "exercise")
public class WeightTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private int weight;

    @Column(nullable = false)
    private int reps;

    public WeightTable() {}

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public int getWeight() { return weight; }
    public int getReps() { return reps; }

    public void setName(String name) { this.name = name; }
    public void setUsername(String username) { this.username = username; }
    public void setCategory(String category) { this.category = category; }
    public void setWeight(int weight) { this.weight = weight; }
    public void setReps(int reps) { this.reps = reps; }
}