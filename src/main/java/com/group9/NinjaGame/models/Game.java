package com.group9.NinjaGame.models;

import com.group9.NinjaGame.entities.Card;
import com.group9.NinjaGame.entities.CardSet;

import java.util.*;

public class Game {
    private UUID id;
    private CardSet selectedCardSet;
    private int points;
    private int miniGameAttempts;
    private int cardsDone;
    private int timeLimit;
    private boolean singlePlayer;
    private boolean playingAlone;

    public Game(){
    }


    public Game(int timeLimit, boolean singlePlayer, boolean playingAlone) {
        this.id = null;
        this.timeLimit = timeLimit;
        this.singlePlayer = singlePlayer;
        this.playingAlone = playingAlone;
        this.selectedCardSet = new CardSet();
        this.miniGameAttempts = getMiniGameAttempts();
        this.cardsDone = getCardsDone();
    }

    public List<Card> getAllCards() {
        if(selectedCardSet.getCards() != null){
            return new ArrayList<>(selectedCardSet.getCards());
        }
        return null;
    }

    public void setAllCards(List<Card> cards) {
        Set set = new HashSet<Card>(cards);
        this.selectedCardSet.setCards(set);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public CardSet getSelectedCardSet() {
        return selectedCardSet;
    }

    public void setSelectedCardSet(CardSet cardSet) {
        this.selectedCardSet = cardSet;
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
        this.selectedCardSet.getCards().removeIf(card -> card.getId().equals(id));
    }
}
