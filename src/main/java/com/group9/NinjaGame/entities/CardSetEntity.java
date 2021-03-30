package com.group9.NinjaGame.entities;

import com.group9.NinjaGame.models.Card;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "card_sets")
public class CardSetEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @Column(name = "name", length = 50, nullable = false, unique = false)
    @NotBlank(message = "Name is mandatory")
    private String name;


    @Column(name = "complete_time_limit", length = 10, nullable = false, unique = false)
    @NotBlank(message = "Time limit is mandatory")
    private int completeTimeLimit;

    @Column(name = "difficulty", length = 10, nullable = false, unique = false)
    @NotBlank(message = "Difficulty is mandatory")
    private int difficulty;

    @Column(name = "multiplayer_suitable", nullable = false, unique = false)
    @NotBlank(message = "This is mandatory")
    private boolean multiplayerSuitable;

    @ManyToMany
    @JoinTable(
            name = "card_set_cards",
            joinColumns = @JoinColumn(name = "card_set_id"),
            inverseJoinColumns = @JoinColumn(name = "card_id"))
    Set<CardEntity> cards;

    @OneToOne(mappedBy = "selectedCardSet")
    private GameEntity gameEntity;


    public CardSetEntity() {
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

    public int getCompleteTimeLimit() {
        return completeTimeLimit;
    }

    public void setCompleteTimeLimit(int completeTimeLimit) {
        this.completeTimeLimit = completeTimeLimit;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public boolean isMultiplayerSuitable() {
        return multiplayerSuitable;
    }

    public void setMultiplayerSuitable(boolean multiplayerSuitable) {
        this.multiplayerSuitable = multiplayerSuitable;
    }

    public Set<CardEntity> getCards() {
        return cards;
    }

    public void setCards(Set<CardEntity> cards) {
        this.cards = cards;
    }
}


