package com.group9.NinjaGame.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CardSet {

    public UUID id;
    public List<Card> listOfCards;
    public String name;
    public int completeTimeLimit;
    public boolean multiplayerSuitable;
    public int difficulty;
    //public boolean userCreated;

    public CardSet(){

    }

    public CardSet (List<Card> listOfCards, String name, int completeTimeLimit, boolean multiplayerSuitable, int difficulty){
        this.id = UUID.randomUUID();
        this.listOfCards = listOfCards;
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

    public List<Card> getListOfCards() {
        return listOfCards;
    }

    public void setListOfCards(List<Card> listOfCards) {
        this.listOfCards = listOfCards;
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
