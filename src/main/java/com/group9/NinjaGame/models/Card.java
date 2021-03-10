package com.group9.NinjaGame.models;

import java.util.UUID;

public class Card {
    public UUID id;
    public String name;
    public String description;
    public int points;

    public Card(String name, String description, int points) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.description = description;
        this.points = points;
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
}

