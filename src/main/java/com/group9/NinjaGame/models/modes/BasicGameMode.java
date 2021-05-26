package com.group9.NinjaGame.models.modes;

import com.group9.NinjaGame.entities.Card;
import com.group9.NinjaGame.models.structural.BossFightScore;
import com.group9.NinjaGame.models.GameInfo;
import com.group9.NinjaGame.models.Player;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class BasicGameMode implements GameMode {

    public static final String GAME_MODE_ID = "basic";

    public UUID playerOnTurn;

    public int score;

    public Card drawnCard;

    public List<Card> remainingCards;

    public List<Player> players;

    public List<UUID> completeStates;

    public List<BossFightScore> bossFightScores;

    public void init(GameInfo gameInfo, List<Card> cards, int timeLimit) {
        playerOnTurn = gameInfo.lobby.players.get(0).sessionId;
        score = 0;
        players = gameInfo.lobby.players;
        drawnCard = null;
        completeStates = new LinkedList<>();
        bossFightScores = new LinkedList<>();
        remainingCards = cards;
    }

    @Override
    public String getGameModeId() {
        return GAME_MODE_ID;
    }

    public void score(UUID playerId, int score) {
        BossFightScore bossFightScore = new BossFightScore(playerId, score);

        bossFightScores.add(bossFightScore);
    }
}
