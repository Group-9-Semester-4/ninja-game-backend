package com.group9.NinjaGame.entities;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Entity
@Table(name = "cards")
public class CardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "name", length = 50, nullable = false, unique = false)
    @NotBlank(message = "Name is mandatory")
    private String name;

    @Column(name = "description", length = 50, nullable = false, unique = false)
    @NotBlank(message = "Description is mandatory")
    private String description;

    @Column(name = "points", length = 10, nullable = false, unique = false)
    @NotBlank(message = "Number of points is mandatory")
    private int points;

    @Column(name = "difficulty_type", nullable = false, unique = false)
    @NotBlank(message = "This field is mandatory")
    private int difficulty_type;

    @Column(name = "difficulty", nullable = false, unique = false)
    @NotBlank(message = "This field is mandatory")
    private int difficulty;

    @Column(name = "filepath", nullable = false, unique = false)
    private String filepath;

    public CardEntity() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getDifficulty_type() {
        return difficulty_type;
    }

    public void setDifficulty_type(int difficulty_type) {
        this.difficulty_type = difficulty_type;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }
}
