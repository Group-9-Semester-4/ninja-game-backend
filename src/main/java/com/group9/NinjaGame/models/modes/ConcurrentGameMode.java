package com.group9.NinjaGame.models.modes;

import com.group9.NinjaGame.entities.Card;
import com.group9.NinjaGame.models.GameInfo;
import com.group9.NinjaGame.models.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ConcurrentGameMode implements GameMode{

    public static final String GAME_MODE_ID = "concurrent";
    public List<Player> players;

    // game time should be in seconds
    public int gameTime;

    // hashmap players and the current card they are playing with now
    public HashMap<UUID, Card> playerCurrentCard;

    //hashmap players and list of remaining cards for each player
    public HashMap<UUID, List<Card>> playerRemainingCards;

    // hashmap storing uuids of all players and their respective scores (points totaling cards done)
    public HashMap<UUID, Integer> playerScores;

    // hashmap storing uuids of all players and the number of cards they completed- to be shown in FE all the time
    public HashMap<UUID, Integer> numberOfPlayerCardsDone;

    // hashmap storing uuds of all players and their bossfight scores to make the final leaderboard
    public HashMap<UUID, Integer> bossFightScores;

    // we need standings to know who finished the game 1st 2nd... to give ammo bonuses
    public List<UUID> standings;

    private List<Card> cards;

    @Override
    public void init(GameInfo gameInfo, List<Card> cards, int timeLimit) {
        players = gameInfo.lobby.players;
        playerCurrentCard = new HashMap<>();
        playerRemainingCards = new HashMap<>();
        playerScores = new HashMap<>();
        numberOfPlayerCardsDone = new HashMap<>();
        bossFightScores = new HashMap<>();
        standings = new ArrayList<>();
        gameTime = timeLimit;

        for (Player player : players) {
            playerRemainingCards.put(player.sessionId, cards);
        }
    }
    @Override
    public String getGameModeId() {
        return GAME_MODE_ID;
    }

}
