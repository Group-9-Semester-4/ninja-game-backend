package com.group9.NinjaGame.models;

import com.group9.NinjaGame.entities.CardEntity;

import javax.persistence.Column;
import java.util.UUID;

public class Card {
    public UUID id;
    public String name;
    public String description;
    public int points;
    private boolean difficulty_type;
    private int difficulty;

    public Card(String name, String description, int points, boolean difficulty_type, int difficulty) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.description = description;
        this.points = points;
        this.difficulty_type = difficulty_type;
        this.difficulty = difficulty;
    }

    public static Card fromCardEntity(CardEntity e) {
        return new Card(e.getName(), e.getDescription(), e.getPoints(), e.isDifficulty_type(), e.getDifficulty());
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

    public boolean isDifficulty_type() {
        return difficulty_type;
    }

    public void setDifficulty_type(boolean difficulty_type) {
        this.difficulty_type = difficulty_type;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }
}

