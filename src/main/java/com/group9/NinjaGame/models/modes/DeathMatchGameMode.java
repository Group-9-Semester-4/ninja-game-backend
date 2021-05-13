package com.group9.NinjaGame.models.modes;

import com.group9.NinjaGame.entities.Card;
import com.group9.NinjaGame.models.GameInfo;
import com.group9.NinjaGame.models.Player;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class DeathMatchGameMode implements GameMode{

    public static final String GAME_MODE_ID = "deathmatch arena";

    public List<Card> remainingCards;

    public List<Player> players;

    // hashmap storing uuids of all players and their respective scores (points totaling cards done)
    public HashMap<UUID, Integer> playerScores;

    // is int sufficient here or do we need list<UUID> or sth?
    public HashMap<UUID, Boolean> playersReady;

    @Override
    public void init(GameInfo gameInfo, List<Card> cards, int timeLimit) {
        players = gameInfo.lobby.players;
        remainingCards = cards;
        playerScores = new HashMap<>();
        playersReady = new HashMap<>();
    }

    @Override
    public String getGameModeId() {
        return GAME_MODE_ID;
    }

    public HashMap<UUID, Boolean> getPlayersReady() {
        return playersReady;
    }
}
