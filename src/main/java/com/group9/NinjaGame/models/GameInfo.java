package com.group9.NinjaGame.models;

import com.group9.NinjaGame.entities.Card;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class GameInfo {

    public UUID gameId;

    public List<Card> remainingCards;

    public List<Player> players;

    public String lobbyCode;

    public boolean started;

    public GameInfo(UUID gameId, String lobbyCode) {

        this.gameId = gameId;
        this.lobbyCode = lobbyCode;

        remainingCards = new LinkedList<>();
        players = new LinkedList<>();
        started = false;
    }

    public boolean removeCardById(UUID cardId) {
        for (Card card : remainingCards) {
            if (card.getId() == cardId) {
                return remainingCards.remove(card);
            }
        }

        return false;
    }

}
