package com.group9.NinjaGame.models;

import com.group9.NinjaGame.entities.CardEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Game {
    private UUID uuid;
    private List<Card> allCards;
    private int points;
    private int timeLimit;
    private boolean singleplayer;
    private boolean playingAlone;

    public Game(int timeLimit, boolean singleplayer, boolean playingAlone) {
        this.uuid = UUID.randomUUID();
        this.timeLimit = timeLimit;
        this.singleplayer = singleplayer;
        this.playingAlone = playingAlone;
        this.allCards = new ArrayList<Card>();
    }

    public List<Card> getAllCards() {
        return allCards;
    }

    public void setAllCards(List<Card> allCards) {
        this.allCards = allCards;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public boolean isSingleplayer() {
        return singleplayer;
    }

    public void setSingleplayer(boolean singleplayer) {
        this.singleplayer = singleplayer;
    }

    public boolean isPlayingAlone() {
        return playingAlone;
    }

    public void setPlayingAlone(boolean playingAlone) {
        this.playingAlone = playingAlone;
    }
}
