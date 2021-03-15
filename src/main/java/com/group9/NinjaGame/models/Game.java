package com.group9.NinjaGame.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Game {
    private UUID id;
    private List<Card> allCards;
    private int points;
    private int miniGameAttempts;
    private int cardsDone;
    private int timeLimit;
    private boolean singlePlayer;
    private boolean playingAlone;

    public Game(int timeLimit, boolean singlePlayer, boolean playingAlone) {
        this.id = UUID.randomUUID();
        this.timeLimit = timeLimit;
        this.singlePlayer = singlePlayer;
        this.playingAlone = playingAlone;
        this.allCards = new ArrayList<Card>();
        this.miniGameAttempts = getMiniGameAttempts();
        this.cardsDone = getCardsDone();
    }

    public List<Card> getAllCards() {
        return allCards;
    }

    public void setAllCards(List<Card> allCards) {
        this.allCards = allCards;
    }

    public UUID getId() {
        return id;
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
        return singlePlayer;
    }

    public void setSingleplayer(boolean singlePlayer) {
        this.singlePlayer = singlePlayer;
    }

    public boolean isPlayingAlone() {
        return playingAlone;
    }

    public void setPlayingAlone(boolean playingAlone) {
        this.playingAlone = playingAlone;
    }

    public int getMiniGameAttempts() {
        if (points == 0 || cardsDone == 0) {
            return 0;
        }
        return (int) points / cardsDone;
    }

    public void setMiniGameAttempts(int miniGameAttempts) {
        this.miniGameAttempts = miniGameAttempts;
    }

    public int getCardsDone() {
        return cardsDone;
    }

    public void setCardsDone(int cardsDone) {
        this.cardsDone = cardsDone;
    }

    public void removeCard(UUID id) {
        this.allCards.removeIf(card -> card.getId().equals(id));
    }
}
