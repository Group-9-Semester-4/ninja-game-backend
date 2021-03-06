package com.group9.NinjaGame.entities;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "card_sets")
public class CardSet {
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

    // TODO: Fix this usage of eager load, it's not the best, should be FetchType.Lazy
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "card_set_cards",
            joinColumns = @JoinColumn(name = "card_set_id"),
            inverseJoinColumns = @JoinColumn(name = "card_id"))
    Set<Card> cards;

    @OneToMany(mappedBy = "selectedCardSet")
    private List<Game> games;

    @Column(name = "temporary", nullable = false)
    private boolean temporary;

    public CardSet() {
    }

    public CardSet(UUID id, String name) {
        this.id = id;
        this.name = name;
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

    public Set<Card> getCards() {
        return cards;
    }

    public void setCards(Set<Card> cards) {
        this.cards = cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = new HashSet<>(cards);
    }

    public boolean isTemporary() {
        return temporary;
    }

    public void setTemporary(boolean temporary) {
        this.temporary = temporary;
    }
}


