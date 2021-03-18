package com.group9.NinjaGame.models;

import com.group9.NinjaGame.entities.CardEntity;

import java.util.UUID;

public class Card {
    public UUID id;
    public String name;
    public String description;
    public int points;
    private boolean difficulty_type;
    private int difficulty;
    private String filepath;
    private static final String absoluteServerPath = "http://localhost:8080/img/card_pictures/";

    public Card() {
    }

    public Card(String name, String description, int points, boolean difficulty_type, int difficulty, String filepath) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.description = description;
        this.points = points;
        this.difficulty_type = difficulty_type;
        this.difficulty = difficulty;
        this.filepath = absoluteServerPath + filepath;
    }

    public static Card fromCardEntity(CardEntity e) {
        Card card = new Card(e.getName(), e.getDescription(), e.getPoints(), e.isDifficulty_type(), e.getDifficulty(), e.getFilepath());
        card.setId(e.getId());
        return card;
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

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }
}

