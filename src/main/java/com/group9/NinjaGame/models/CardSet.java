package com.group9.NinjaGame.models;

import java.util.List;
import java.util.UUID;

public class CardSet {

    public UUID id;
    public List<Card> cards;
    public String name;
    public int completeTimeLimit;
    public boolean multiplayerSuitable;
    public int difficulty;
    //public boolean userCreated;

    public CardSet(){

    }

    public CardSet (List<Card> cards, String name, int completeTimeLimit, boolean multiplayerSuitable, int difficulty){
        this.id = UUID.randomUUID();
        this.cards = cards;
        this.name = name;
        this.completeTimeLimit = completeTimeLimit;
        this.multiplayerSuitable = multiplayerSuitable;
        this.difficulty = difficulty;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
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

    public boolean isMultiplayerSuitable() {
        return multiplayerSuitable;
    }

    public void setMultiplayerSuitable(boolean multiplayerSuitable) {
        this.multiplayerSuitable = multiplayerSuitable;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }
}
