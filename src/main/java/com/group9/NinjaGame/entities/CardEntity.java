package com.group9.NinjaGame.entities;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name="cards")
public class CardEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private UUID id;

    @Column(name="name", length=50, nullable=false, unique=false)
    private String name;

    @Column(name="description", length=50, nullable=false, unique=false)
    private String description;

    @Column(name="points", length=10, nullable=false, unique=false)
    private String points;

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

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }
}