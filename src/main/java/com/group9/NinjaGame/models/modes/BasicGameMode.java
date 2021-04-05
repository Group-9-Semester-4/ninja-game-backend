package com.group9.NinjaGame.models.modes;

import com.group9.NinjaGame.entities.Card;
import com.group9.NinjaGame.models.GameInfo;
import com.group9.NinjaGame.models.Player;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class BasicGameMode implements GameMode {

    public UUID playerOnTurn;

    public Card drawnCard;

    public List<Card> remainingCards;

    public List<Player> players;

    public HashMap<UUID, Boolean> completeStates;


    @Override
    public void setCards(List<Card> cards) {
        remainingCards = cards;
    }

    @Override
    public void init(GameInfo gameInfo) {
        playerOnTurn = gameInfo.lobby.players.get(0).sessionId;
        drawnCard = null;
        completeStates = new HashMap<>();
    }
}
