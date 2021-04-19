package com.group9.NinjaGame.models.modes;

import com.group9.NinjaGame.entities.Card;
import com.group9.NinjaGame.models.GameInfo;
import com.group9.NinjaGame.models.Player;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class BasicGameMode implements GameMode {

    public static final String GAME_MODE_ID = "basic";

    public UUID playerOnTurn;

    public Card drawnCard;

    public List<Card> remainingCards;

    public List<Player> players;

    public HashMap<UUID, Boolean> completeStates;

    public HashMap<UUID, Integer> bossFightScores;

    @Override
    public void setCards(List<Card> cards) {
        remainingCards = cards;
    }
    @Override
    public List<Card> getCards() {
        return remainingCards;
    }
    @Override
    public void init(GameInfo gameInfo) {
        playerOnTurn = gameInfo.lobby.players.get(0).sessionId;
        players = gameInfo.lobby.players;
        drawnCard = null;
        completeStates = new HashMap<>();
        bossFightScores = new HashMap<>();
    }

    @Override
    public String getGameModeId() {
        return GAME_MODE_ID;
    }
}
